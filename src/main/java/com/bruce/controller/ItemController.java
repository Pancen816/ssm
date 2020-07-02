package com.bruce.controller;

import com.bruce.entity.Item;
import com.bruce.enums.ExceptionInfoEnum;
import com.bruce.exception.SsmException;
import com.bruce.service.ItemService;
import com.bruce.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 商品controller类
 * @Project ssm
 * @Package com.bruce.controller
 * @ClassName ItemController
 * @Author Bruce
 * @Date 2020/6/28 23:47
 * @Version 1.0
 **/
@Controller
@Slf4j
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @Value("${pic.maxSize}")
    private Long picMaxSize;

    @Value("${pic.types}")
    private String picTypes;

    /**
     * 商品分页展示
     * @author Bruce
     * @date 2020/6/29 19:53
     * @param name  商品名称
     * @param page  当前页
     * @param size  每页显示条数
     * @param model request域对象
     * @return java.lang.String
     */
    @GetMapping("/list")
    public String list(String name, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "4") Integer size, Model model){

        // 调用itemService查询，并获得PageInfo
        PageInfo<Item> pageInfo = itemService.findProductByNameLimit(name, page, size);
        // 将数据放到request域中
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("name",name);
        // 转发到item_list页面
        return "item/item_list";
    }

    /**
     * 跳转添加商品页面
     * @author Bruce
     * @date 2020-06-30 15:58
     * @param
     * @return java.lang.String
     */
    @GetMapping("/add-ui")
    public String addUI(){
        return "item/item_add";
    }

    /**
     * 添加商品信息
     * @author Bruce
     * @date 2020-07-01 00:08
     * @param picFile   图片文件
     * @param item      商品对象
     * @param bindingResult 错误结果信息
     * @param request  request域对象
     * @return com.bruce.vo.ResultVO
     */
    @PostMapping("/add")
    @ResponseBody
    public ResultVO addItem(MultipartFile picFile, @Valid Item item, BindingResult bindingResult,
                            HttpServletRequest request) throws IOException {
        // 文件上传项  非空判断
        if (picFile == null || picFile.getSize() == 0){
            // 没有文件上传项
            log.info("【添加商品】商品图片为必传项，岂能不传！");
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"商品图片为必传项，岂能不传！");
        }
        // 文件大小判断
        if (picFile.getSize() > picMaxSize){
            log.info("【添加商品】商品图片内存过大！size = {}",picFile.getSize());
            throw new SsmException(ExceptionInfoEnum.PIC_GREATER);
        }
        // 文件类型判断
        List<String> typeList = Arrays.asList(picTypes.split(","));
        String filename = picFile.getOriginalFilename();
        String typeName = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!typeList.contains(typeName)){
            log.info("【添加商品】商品图片类型不正确 typeName = {}",typeName);
            throw new SsmException(ExceptionInfoEnum.PIC_TYPE_ERROE);
        }
        // 文件是否损坏
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if (image == null){
            log.info("【添加商品】商品图片已损坏 picFile = {}",picFile);
            throw new SsmException(ExceptionInfoEnum.PIC_BREAK);
        }
        // 新名字
        String newName = UUID.randomUUID().toString() + "." + typeName;
        String path = request.getServletContext().getRealPath("/") + "static/images/" + newName;
        File file = new File(path);
        // 判断文件上一级目录是否存在
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 保存到本地  picFile.transferTo(file)  临时文件copy的错误
        IOUtils.copy(picFile.getInputStream(), new FileOutputStream(file));
        // 将图片的访问路径设置到item中
        String pic = request.getContextPath() + "/static/images/" + newName;
        item.setPic(pic);
        // 校验普通表单
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【添加商品】参数不正确！msg = {}",msg);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),msg);
        }
        // 调用itemService进行保存
        itemService.save(item);
        return new ResultVO(0,"成功",null);
    }


    /**
     * 根据id删除指定商品信息
     * @author Bruce
     * @date 2020-07-01 00:05
     * @param id  商品id
     * @return com.bruce.vo.ResultVO
     */
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResultVO deleteItem(@PathVariable Integer id){
//        System.out.println(id);
        // 调用itemService执行删除
        itemService.deleteById(id);
        return new ResultVO(0,"成功",null);
    }

    /**
     * 跳转商品修改页面
     * @author Bruce
     * @date 2020-07-01 00:33
     * @param
     * @return java.lang.String
     */
    @GetMapping("/update-ui")
    public String updateUI(Integer id,Model model){

        Item item = itemService.findById(id);

        model.addAttribute("item", item);

         return "item/item_update";
    }

    /**
     * 修改商品
     * @author Bruce
     * @date 2020-07-01 02:44
     * @param picFile  图片
     * @param item     商品对象
     * @param bindingResult  错误结果集
     * @param request     request域对象
     * @return com.bruce.vo.ResultVO
     */
    @PostMapping("/update")
    @ResponseBody
    public ResultVO updateItem(MultipartFile picFile, @Valid Item item, BindingResult bindingResult,
                            HttpServletRequest request) throws IOException {
        // 文件上传项  非空判断
        if (picFile == null || picFile.getSize() == 0){
            // 没有文件上传项
            log.info("【修改商品】商品图片为必传项，岂能不传！");
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"商品图片为必传项，岂能不传！");
        }
        // 文件大小判断
        if (picFile.getSize() > picMaxSize){
            log.info("【修改商品】商品图片内存过大！size = {}",picFile.getSize());
            throw new SsmException(ExceptionInfoEnum.PIC_GREATER);
        }
        // 文件类型判断
        List<String> typeList = Arrays.asList(picTypes.split(","));
        String filename = picFile.getOriginalFilename();
        String typeName = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        if (!typeList.contains(typeName)){
            log.info("【修改商品】商品图片类型不正确 typeName = {}",typeName);
            throw new SsmException(ExceptionInfoEnum.PIC_TYPE_ERROE);
        }
        // 文件是否损坏
        BufferedImage image = ImageIO.read(picFile.getInputStream());
        if (image == null){
            log.info("【修改商品】商品图片已损坏 picFile = {}",picFile);
            throw new SsmException(ExceptionInfoEnum.PIC_BREAK);
        }
        // 新名字
        String newName = UUID.randomUUID().toString() + "." + typeName;
        String path = request.getServletContext().getRealPath("/") + "static/images/" + newName;
        File file = new File(path);
        // 判断文件上一级目录是否存在
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        // 保存到本地  picFile.transferTo(file)  临时文件copy的错误
        IOUtils.copy(picFile.getInputStream(), new FileOutputStream(file));
        // 将图片的访问路径设置到item中
        String pic = request.getContextPath() + "/static/images/" + newName;
        item.setPic(pic);
        // 校验普通表单
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【修改商品】参数不正确！msg = {}",msg);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),msg);
        }
        // 调用itemService进行保存
        itemService.updateItem(item);
        return new ResultVO(0,"成功",null);
    }
}

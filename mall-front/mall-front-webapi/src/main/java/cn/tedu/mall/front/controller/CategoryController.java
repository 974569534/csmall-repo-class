package cn.tedu.mall.front.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @date 2022/8/1 15:26:04
 */
@Api(tags = "前台分类查询")
@RestController
@RequestMapping("/front/category")
public class CategoryController {

    @Autowired
    private IFrontCategoryService iFrontCategoryService;

    @GetMapping("/all")
    @ApiOperation("查询所有三级分类树")
    public JsonResult<FrontCategoryTreeVO<FrontCategoryEntity>> getTreeVO(){

        FrontCategoryTreeVO<FrontCategoryEntity> treeVO = iFrontCategoryService.categoryTree();

        return JsonResult.ok(treeVO);
    }

}

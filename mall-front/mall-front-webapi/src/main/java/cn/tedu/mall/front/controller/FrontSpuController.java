package cn.tedu.mall.front.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontProductService;
import cn.tedu.mall.pojo.product.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/2 09:51:24
 */
@Api(tags = "前台商品spu模块")
@RestController
@RequestMapping("/front/spu")
public class FrontSpuController {

    @Autowired
    private IFrontProductService iFrontProductService;

    @GetMapping("/list/{categoryId}")
    @ApiOperation("根据分类id查询分页spu列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "分类id", name = "categoryId", example = "1", required = true, dataType = "long"),
            @ApiImplicitParam(value = "页码", name = "page", example = "1"),
            @ApiImplicitParam(value = "条数", name = "pageSize", example = "10")
    })
    public JsonResult<JsonPage<SpuListItemVO>> listSpuByPage(@PathVariable("categoryId") Long categoryId, Integer page, Integer pageSize){

        JsonPage<SpuListItemVO> list = iFrontProductService.listSpuByCategoryId(categoryId, page, pageSize);
        return JsonResult.ok(list);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据spuId查询spu")
    @ApiImplicitParam(value = "spuId", name = "spuId", example = "1", required = true, dataType = "long")
    public JsonResult getFrontSpuById(@PathVariable Long id){

        SpuStandardVO spuVO = iFrontProductService.getFrontSpuById(id);
        return JsonResult.ok(spuVO);
    }

    @GetMapping("/template/{id}")
    @ApiOperation("根据spuId查询attribute")
    @ApiImplicitParam(value = "spuId", name = "spuId", example = "1", required = true, dataType = "long")
    public JsonResult getSpuAttributesBySpuId(@PathVariable Long id){

        List<AttributeStandardVO> attributes = iFrontProductService.getSpuAttributesBySpuId(id);
        return JsonResult.ok(attributes);
    }

}

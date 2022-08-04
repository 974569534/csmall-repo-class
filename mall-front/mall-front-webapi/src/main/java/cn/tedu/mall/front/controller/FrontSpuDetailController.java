package cn.tedu.mall.front.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontProductService;
import cn.tedu.mall.pojo.product.vo.SpuDetailStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * @date 2022/8/2 12:00:06
 */
@Api(tags = "前台商品spuDetail模块")
@RestController
@RequestMapping("/front/spu/detail")
public class FrontSpuDetailController {

    @Autowired
    private IFrontProductService iFrontProductService;

    @GetMapping("/{spuId}")
    @ApiOperation("根据spuId查询spu详情")
    @ApiImplicitParam(value = "spuId", name = "spuId", example = "1", required = true, dataType = "long")
    public JsonResult getSpuDetail(@PathVariable Long spuId){

        SpuDetailStandardVO spuDetail = iFrontProductService.getSpuDetail(spuId);
        return JsonResult.ok(spuDetail);
    }

}

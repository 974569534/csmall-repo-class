package cn.tedu.mall.front.controller;

import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.front.service.IFrontProductService;
import cn.tedu.mall.pojo.product.vo.SkuStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/2 11:58:13
 */
@Api(tags = "前台商品sku模块")
@RestController
@RequestMapping("/front/sku")
public class FrontSkuController {

    @Autowired
    private IFrontProductService iFrontProductService;

    @GetMapping("/{spuId}")
    @ApiOperation("根据spuId查询所有sku")
    @ApiImplicitParam(value = "spuId", name = "spuId", example = "1", required = true, dataType = "long")
    public JsonResult getFrontSkusBySpuId(@PathVariable("spuId") Long spuId){
        List<SkuStandardVO> skus = iFrontProductService.getFrontSkusBySpuId(spuId);
        return JsonResult.ok(skus);
    }

}

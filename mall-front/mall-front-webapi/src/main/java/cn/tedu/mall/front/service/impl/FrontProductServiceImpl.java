package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.front.service.IFrontProductService;
import cn.tedu.mall.pojo.product.vo.*;
import cn.tedu.mall.product.service.front.IForFrontAttributeService;
import cn.tedu.mall.product.service.front.IForFrontSkuService;
import cn.tedu.mall.product.service.front.IForFrontSpuService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/2 09:42:36
 */
@Service
public class FrontProductServiceImpl implements IFrontProductService {

    // 声明消费spu相关业务逻辑
    @DubboReference
    private IForFrontSpuService dubboSpuService;

    // 声明消费spu相关业务逻辑
    @DubboReference
    private IForFrontSkuService dubboSkuService;

    // 声明消费商品参数选项attribute相关业务逻辑
    @DubboReference
    private IForFrontAttributeService dubboAttributeService;

    @Override
    public JsonPage<SpuListItemVO> listSpuByCategoryId(Long categoryId, Integer page, Integer pageSize) {
        JsonPage<SpuListItemVO> list = dubboSpuService.listSpuByCategoryId(categoryId, page, pageSize);
        return list;
    }

    @Override
    public SpuStandardVO getFrontSpuById(Long id) {
        SpuStandardVO spuVO = dubboSpuService.getSpuById(id);
        return spuVO;
    }

    @Override
    public List<SkuStandardVO> getFrontSkusBySpuId(Long spuId) {
        List<SkuStandardVO> spuVOList = dubboSkuService.getSkusBySpuId(spuId);
        return spuVOList;
    }

    @Override
    public SpuDetailStandardVO getSpuDetail(Long spuId) {
        SpuDetailStandardVO spuDetailVO = dubboSpuService.getSpuDetailById(spuId);
        return spuDetailVO;
    }

    @Override
    public List<AttributeStandardVO> getSpuAttributesBySpuId(Long spuId) {
        List<AttributeStandardVO> attributes = dubboAttributeService.getSpuAttributesBySpuId(spuId);
        return attributes;
    }
}

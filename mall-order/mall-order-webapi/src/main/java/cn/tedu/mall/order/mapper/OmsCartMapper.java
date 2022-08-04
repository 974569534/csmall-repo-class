package cn.tedu.mall.order.mapper;

import cn.tedu.mall.pojo.order.dto.CartAddDTO;
import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/2 16:22:17
 */
@Repository
public interface OmsCartMapper {

    /**
     * 判断购物车中是否已经包含指定sku的商品
     * @param userId
     * @param skuId
     * @return
     */
    OmsCart selectExistsCart(@Param("userId") Long userId,
                             @Param("skuId") Long skuId);

    /**
     * 新增sku信息到购物车
     * @param omsCart
     * @return
     */
    void saveCart(OmsCart omsCart);

    /**
     * 修改购物车中指定sku的数量
     * @param omsCart
     * @return
     */
    void updateQuantityById(OmsCart omsCart);

    /**
     * 根据用户id查询购物车中sku信息
     * @param userId
     * @return
     */
    List<CartStandardVO> selectCartByUserId(@Param("userId") Long userId);

    /**
     * 根据参数中的id,删除购物车中的商品(支持批量删除)
     * @param ids
     * @return
     */
    int deleteCartsByIds(Long[] ids);

    /**
     * 根据用户名id清空购物车
     * @param userId
     * @return
     */
    int deleteAllCartsByUserId(@Param("userId") Long userId);

    /**
     * 根据userId和skuId删除商品
     * @param userId
     * @param skuId
     */
    int deleteCartByUserIdAndSkuId(@Param("userId")Long userId,@Param("skuId")Long skuId);

}

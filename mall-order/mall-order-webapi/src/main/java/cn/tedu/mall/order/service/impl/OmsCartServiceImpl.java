package cn.tedu.mall.order.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.pojo.domain.CsmallAuthenticationInfo;
import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.order.mapper.OmsCartMapper;
import cn.tedu.mall.order.service.IOmsCartService;
import cn.tedu.mall.pojo.order.dto.CartAddDTO;
import cn.tedu.mall.pojo.order.dto.CartUpdateDTO;
import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/2 16:54:01
 */
@Service
public class OmsCartServiceImpl implements IOmsCartService {

    @Autowired
    private OmsCartMapper omsCartMapper;

    @Override
    public void addCart(CartAddDTO cartDTO) {
        // 获得当前登录用户的id
        Long userId = this.getUserId();
        // 查询这个userId的用户是否已经将指定的sku添加到购物车
        OmsCart omsCart = omsCartMapper.selectExistsCart(userId, cartDTO.getSkuId());
        if(omsCart != null){
            // 如果omsCart不是空,说明购物车已存在该sku,需要修改该sku数量即可
            // 将omsCart对象中的quantity和cartDTO的quantity相加,最后赋值给omsCart的属性
            omsCart.setQuantity(omsCart.getQuantity()+ cartDTO.getQuantity());
            omsCartMapper.updateQuantityById(omsCart);
        }else {
            // 将cartDTO属性赋值给omsCart
            omsCart = new OmsCart();
            BeanUtils.copyProperties(cartDTO, omsCart);
            omsCart.setUserId(userId);
            omsCartMapper.saveCart(omsCart);
        }
    }

    /**
     * 根据用户id分页查询用户购物车sku信息
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public JsonPage<CartStandardVO> listCarts(Integer page, Integer pageSize) {
        Long userId = getUserId();
        // PageHelper框架设置分页条件
        PageHelper.startPage(page,pageSize);
        List<CartStandardVO> list = omsCartMapper.selectCartByUserId(userId);
        // 将分页结果对象PageInfo转换为JsonPage返回
        return  JsonPage.restPage(new PageInfo<>(list));
    }

    @Override
    public void removeCart(Long[] ids) {
        int row = omsCartMapper.deleteCartsByIds(ids);
        if(row == 0){
            throw new CoolSharkServiceException(ResponseCode.NOT_FOUND,"要删除的商品不存在");
        }

    }

    @Override
    public void removeAllCarts() {
        int row = omsCartMapper.deleteAllCartsByUserId(getUserId());
        if(row == 0){
            throw new CoolSharkServiceException(ResponseCode.NOT_FOUND,"购物车没有商品");
        }
    }

    @Override
    public void removeUserCarts(OmsCart omsCart) {
        Long userId = getUserId();
        Long skuId = omsCart.getSkuId();
        int row = omsCartMapper.deleteCartByUserIdAndSkuId(userId, skuId);
        if(row == 0){
            throw new CoolSharkServiceException(ResponseCode.NOT_FOUND,"商品不存在!");
        }
    }

    /**
     * 修改购物车商品数量
     * @param cartUpdateDTO
     */
    @Override
    public void updateQuantity(CartUpdateDTO cartUpdateDTO) {
        // 将CartUpdateDTO值赋值给OmsCart
        OmsCart omsCart = new OmsCart();
        BeanUtils.copyProperties(cartUpdateDTO,omsCart);
        // 实现持久层调用
        omsCartMapper.updateQuantityById(omsCart);
    }

    // 获取SpringSecurity提供登录用户的容器
    public CsmallAuthenticationInfo getUserInfo(){
        // 获得SpringSecurity上下文(容器)对象
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        // 如果authenticationToken为空,抛出异常
        if(authenticationToken == null){
            throw new CoolSharkServiceException(ResponseCode.UNAUTHORIZED,"没有登录信息");
        }
        // 如果authenticationToken不为空,获得其中用户信息
        CsmallAuthenticationInfo csmallAuthenticationInfo = (CsmallAuthenticationInfo) authenticationToken.getCredentials();
        return csmallAuthenticationInfo;
    }

    // 为了方便使用userId,定义一个方法来返回
    public Long getUserId(){
        return getUserInfo().getId();
    }

}

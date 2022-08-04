package cn.tedu.mall.order.controller;

import cn.tedu.mall.common.restful.JsonPage;
import cn.tedu.mall.common.restful.JsonResult;
import cn.tedu.mall.order.service.IOmsCartService;
import cn.tedu.mall.order.utils.WebConsts;
import cn.tedu.mall.pojo.order.dto.CartAddDTO;
import cn.tedu.mall.pojo.order.dto.CartUpdateDTO;
import cn.tedu.mall.pojo.order.model.OmsCart;
import cn.tedu.mall.pojo.order.vo.CartStandardVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Administrator
 * @date 2022/8/2 17:40:33
 */
@Api(tags = "购物车模块")
@RestController
@RequestMapping("/oms/cart")
public class OmsCartController {

    @Autowired
    private IOmsCartService omsCartService;

    @PostMapping("/add")
    @ApiOperation("新增购物车")
    // 判断当前用户是否登录,并具备普通用户权限ROLE_USER
    // 访问前台的普通用户,在sso服务器登录获得JWT时,就已经在权限列表中添加ROLE_user的权限了
    @PreAuthorize("hasAuthority('ROLE_user')")
    // @Validated注解激活对应类型的验证过程
    // 如果验证不通过会由统一异常处理类中BindException进行异常处理
    public JsonResult addCart(@Validated CartAddDTO cartAddDTO){
        omsCartService.addCart(cartAddDTO);
        return JsonResult.ok("新增成功");
    }

    @GetMapping("/list")
    @ApiOperation("根据用户名id分页查询用户购物车sku信息")
    @PreAuthorize("hasRole('user')")
    //@PreAuthorize("hasAuthority('ROLE_user')")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "页码", name = "page", example = "1", dataType = "int"),
            @ApiImplicitParam(value = "条数", name = "pageSize", example = "5", dataType = "int")
    })
    public JsonResult<JsonPage<CartStandardVO>> selectCartByUserId(
            // 控制器参数中,实际上也可以判断某个属性是否为空,并给定默认值
            @RequestParam(required = false,defaultValue = WebConsts.DEFAULT_PAGE) Integer page,
            @RequestParam(required = false,defaultValue = WebConsts.DEFAULT_PAGE_SIZE) Integer pageSize){
        JsonPage<CartStandardVO> listCarts = omsCartService.listCarts(page, pageSize);
        return JsonResult.ok(listCarts);
    }

    @PostMapping("/delete")
    @ApiOperation("根据id数组删除购物车中sku商品")
    @ApiImplicitParam(value = "要删除的购物车id数组", name = "ids",required = true,dataType = "array")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult removeCartsByIds(Long[] ids){
        omsCartService.removeCart(ids);
        return JsonResult.ok("删除成功!");
    }

    @GetMapping("/delete/allCarts")
    @ApiOperation("清空购物车")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult removeAllCarts(){
        omsCartService.removeAllCarts();
        return JsonResult.ok("已清空购物车!");
    }

    @PostMapping("/update/quantity")
    @ApiOperation("修改购物车数量")
    @PreAuthorize("hasRole('ROLE_user')")
    public JsonResult updateQuantity(@RequestBody @Validated CartUpdateDTO cartUpdateDTO){
        omsCartService.updateQuantity(cartUpdateDTO);
        return JsonResult.ok("修改购物车数量成功!");
    }

//    @PostMapping("/delete/userCarts")
//    @ApiOperation("根据用户id和skuId删除商品")
//    @PreAuthorize("hasRole('ROLE_user')")
//    public JsonResult removeUserCarts(OmsCart omsCart){
//        omsCartService.removeUserCarts(omsCart);
//        return JsonResult.ok("已删除商品!");
//    }


}

package cn.tedu.mall.order.mapper;

import cn.tedu.mall.pojo.order.model.OmsOrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2022/8/3 17:24:58
 */
@Repository
public interface OmsOrderItemMapper {

    /**
     * 新增订单项方法,一个订单可以包含多个订单,所以将此方法参数设计为List
     * @param omsOrderItems
     * @return
     */
    void insertOrderItems(List<OmsOrderItem> omsOrderItems);

}

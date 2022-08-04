package cn.tedu.mall.order.mapper;

import cn.tedu.mall.pojo.order.model.OmsOrder;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2022/8/3 17:44:13
 */
@Repository
public interface OmsOrderMapper {

    /**
     * 新增订单
     * @param omsOrder
     */
    void insertOrder(OmsOrder omsOrder);

}

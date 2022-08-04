package cn.tedu.mall.front.service.impl;

import cn.tedu.mall.common.exception.CoolSharkServiceException;
import cn.tedu.mall.common.restful.ResponseCode;
import cn.tedu.mall.front.service.IFrontCategoryService;
import cn.tedu.mall.pojo.front.entity.FrontCategoryEntity;
import cn.tedu.mall.pojo.front.vo.FrontCategoryTreeVO;
import cn.tedu.mall.pojo.product.vo.CategoryStandardVO;
import cn.tedu.mall.product.service.front.IForFrontCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @date 2022/8/1 10:39:35
 */
@DubboService
@Service
@Slf4j
public class FrontCategoryServiceImpl implements IFrontCategoryService {

    // 开发规范标准,为了降低Redis的Key拼写错误的风险,我们都会定义常量
    public static final String CATEGORY_TYPE_KEY = "category_tree";

    //当前模块查询所有分类信息对象要依靠product模块,所以需要dubbo调用product模块的查询数据表中所有分类的方法
    @DubboReference
    private IForFrontCategoryService dubboCategoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public FrontCategoryTreeVO categoryTree() {

        // 将查询到的三级分类树保存在Redis中,所有先检查Redis中是否包含上面定义的key
        if(redisTemplate.hasKey(CATEGORY_TYPE_KEY)){
            // redis中已经存在,直接从redis中获得返回即可
            FrontCategoryTreeVO<FrontCategoryEntity> treeVO = (FrontCategoryTreeVO<FrontCategoryEntity>) redisTemplate.boundValueOps(CATEGORY_TYPE_KEY).get();
            return treeVO;
        }

        // Redis中没有三级分类树信息,当前请求是第一个运行该方法的请求
        // dubbo调用查询数据库中所有分类信息对象
        List<CategoryStandardVO> categoryList = dubboCategoryService.getCategoryList();
        // 调用将数据中查询出的所有分类信息三级分类
        FrontCategoryTreeVO<FrontCategoryEntity> treeVO = initTree(categoryList);
        // 将分类信息存储到redis中
        redisTemplate.boundValueOps(CATEGORY_TYPE_KEY).set(treeVO, 25, TimeUnit.HOURS);

        return treeVO;
    }

    private FrontCategoryTreeVO<FrontCategoryEntity> initTree(List<CategoryStandardVO> categoryList) {
        // 确定所有分类对象的父分类
        // 声明一个Map,使用父分类id做这个map的key,使用当前分类对象集合对这个map的value
        // 将所有相同父分类的对象添加到正确的集合中
        Map<Long,List<FrontCategoryEntity>> map = new HashMap<>();
        log.debug("当前分类对象总数:{}",categoryList.size());
        // 遍历categoryStandardVO,进行操作
        for (CategoryStandardVO categoryStandardVO : categoryList) {
            // CategoryStandardVO没有children属性,不能保存子分类
            // 所以先要创建FrontCategoryEntity
            FrontCategoryEntity frontCategoryEntity = new FrontCategoryEntity();
            // 利用复制工具类赋值
            BeanUtils.copyProperties(categoryStandardVO,frontCategoryEntity);
            // 将父id需要取出来,后续需要频繁使用
            Long parentId = frontCategoryEntity.getParentId();
            // 根据当前分类对象父id向map添加,先判断是否存在此key
            if(map.containsKey(parentId)){
                // 如果key存在,则直接将分类对象添加到map.value的list中
                map.get(parentId).add(frontCategoryEntity);
            }else{
                // 当前没有此key,则创建一个list将分类对象存入
                List<FrontCategoryEntity> list = new ArrayList<>();
                list.add(frontCategoryEntity);
                // 使用当前parentId做key,将list存入value中
                map.put(parentId,list);
            }
        }
        // 将子分类对象关联到父分类对象的children属性中
        // 获得map已经包含了所有父分类包含的子分类对象
        // 可以根据根分类开始,通过循环遍历每个级别的分类对象,添加到对应级别的children属性中
        // 一级分类父id为0,获得所有一级分类下的子分类
        List<FrontCategoryEntity> firstLevel = map.get(0L);
        if(firstLevel == null){
            throw new CoolSharkServiceException(ResponseCode.INTERNAL_SERVER_ERROR,"当前项目没有根分类");
        }
        // 遍历所有一级分类对象
        for (FrontCategoryEntity oneLevels : firstLevel) {
            // 获得当前一级分类的id
            Long secondLevelParentId = oneLevels.getId();
            // 获得当前分类对象的所有子分类(获得二级分类集合)
            List<FrontCategoryEntity> secondLevels = map.get(secondLevelParentId);
            // 判断是否包含二级分类对象
            if(secondLevels == null){
                log.warn("当前分类缺少二级分类:{}",secondLevelParentId);
                // 跳过本次循环,继续下面循环
                continue;
            }
            // 遍历二级分类集合
            for (FrontCategoryEntity twoLevel : secondLevels) {
                // 获得当前二级分类的id
                Long thirdLevelParentId = twoLevel.getId();
                // 获得当前分类对象的所有子分类(获得二级分类集合)
                List<FrontCategoryEntity> thirdLevels = map.get(thirdLevelParentId);
                // 判断是否包含三级分类对象
                if(thirdLevels == null){
                    log.warn("当前分类缺少三级分类:{}",thirdLevelParentId);
                    continue;
                }
                // 将三级分类集合添加到二级分类对象的children属性中
                twoLevel.setChildrens(thirdLevels);
            }
            oneLevels.setChildrens(secondLevels);
        }
        // 所有父分类下的子分类都已经添加到children中
        FrontCategoryTreeVO<FrontCategoryEntity> treeVO = new FrontCategoryTreeVO<>();
        treeVO.setCategories(firstLevel);

        return treeVO;
    }


}

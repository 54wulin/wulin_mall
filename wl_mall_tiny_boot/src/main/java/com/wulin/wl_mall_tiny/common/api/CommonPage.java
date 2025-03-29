package com.wulin.wl_mall_tiny.common.api;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @description 分页数据封装类
 *
 */
public class CommonPage<T> {
    private Integer pageNum;
    private Integer pageSize;
    private Integer totalPage;
    private Long total;
    private List<T> list;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }


    /**
     * 将PageHelper分页后的list转为分页信息
     * @param list
     * @return
     * @param <T>
     */
    public static <T> CommonPage<T> restPage(List<T> list){
        CommonPage<T> result =  new CommonPage<T>();
        PageInfo<T> pageInfo = new PageInfo<T>(list);

        result.setTotalPage(pageInfo.getPages());
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        result.setList(pageInfo.getList());

        return result;

    }

}


/*
* 为什么要有这个类呢，
* 首先这个是一个工具类，存放在commn文件夹下面，
* 其次这个类的属性都是  pagenum，pagesize，totalpage，total，list
* pagenum,
*
* 总的来说，这是一个用于封装分页数据的工具类，他的作用是将分页查询的结果进行统一封装，以便于在接口返回数据时使用标准的格式进行输出，特别是在涉及到
* 分页查询时，
* page是当前页码，pagesize是每页显示的记录数，totalpage是总页数，total是总记录数，list是当前页面的数据列表。
*
*这个类通过restpage方法接收分页查询返回的列表，并将其转化为commonpage对象，这可以方便后续的分页数据统一输出，简化了分页的处理过程
* restpage方法，使用pageinfo对象，这个是pagehelper插件分页后的数据容器，将分页信息提取并存入commonpage对象
* 这个方法的返回值是commonpage类型，可以直接用来进行统一的数据返回格式。
*
*
*
* */


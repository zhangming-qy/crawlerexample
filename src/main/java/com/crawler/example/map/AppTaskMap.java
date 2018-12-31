package com.crawler.example.map;

import com.crawler.example.entity.AppTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppTaskMap {

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url,created_time, modified_time from app_tasks where status = #{status}")
    public List<AppTask> getAppTasksByStatus(String status);

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url,created_time, modified_time from app_tasks where root_url = #{root_url}")
    public List<AppTask> getAppTasksByRootUrl(String root_url);

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url, created_time, modified_time from app_tasks")
    public List<AppTask> getAll();

    @Insert("insert into app_tasks(root_url,group_name,order_num,jclass,status,last_url,created_time, modified_time) values (#{root_url},#{group_name},#{order_num},#{jclass},#{status},#{last_url},now(),now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(AppTask appTask);

    @Update("UPDATE app_tasks SET status=#{status},curr_url =#{curr_url},last_url=#{last_url}, modified_time=now() WHERE id =#{id}")
    void update(AppTask appTask);

}


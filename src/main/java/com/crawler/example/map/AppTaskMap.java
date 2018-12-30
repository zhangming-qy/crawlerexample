package com.crawler.example.map;

import com.crawler.example.entity.AppTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AppTaskMap {

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, created_time, modified_time from app_tasks where status = #{status}")
    public List<AppTask> getAppTasksByStatus(String status);

    @Update("UPDATE app_tasks SET status=#{status},curr_url =#{curr_url}, modified_time=#{modified_time} WHERE id =#{id}")
    void update(AppTask appTask);

}


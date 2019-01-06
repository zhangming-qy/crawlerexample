package com.crawler.example.map;

import com.crawler.example.entity.AppTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppTaskMap {

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url,created_time, modified_time from app_tasks where status = #{status} order by order_num")
    public List<AppTask> getAppTasksByStatus(String status);

    @Select("<script>select id, root_url, group_name, order_num, jclass, status, curr_url, last_url,created_time, modified_time from app_tasks where status in " +
            "<foreach item='item' index='index' collection='statusList' open='(' separator=', ' close=')'>" +
            "#{item}" +
            "</foreach>" +
            "order by field(status," +
            "<foreach item='item' index='index' collection='statusList' open='' separator=',' close=''>" +
            "#{item}" +
            "</foreach>" +
            "), order_num</script>")
    List<AppTask> getAppTasksInAndOrderByStatus(@Param("statusList") List<String> statusList);

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url,created_time, modified_time from app_tasks where root_url = #{root_url} and jclass=#{jclass}")
    List<AppTask> getAppTasksByRootUrlAndJClass(@Param("root_url") String root_url, @Param("jclass") String jclass);

    @Select("select id, root_url, group_name, order_num, jclass, status, curr_url, last_url, created_time, modified_time from app_tasks")
    List<AppTask> getAll();

    @Insert("insert into app_tasks(root_url,group_name,order_num,jclass,status,last_url,created_time, modified_time) values (#{root_url},#{group_name},#{order_num},#{jclass},#{status},#{last_url},now(),now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer insert(AppTask appTask);

    @Update("update app_tasks set status=#{status}, curr_url=#{curr_url}, last_url=#{last_url}, modified_time=now() where id=#{id}")
    void update(AppTask appTask);

}


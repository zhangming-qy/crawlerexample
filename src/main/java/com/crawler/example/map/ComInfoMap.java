package com.crawler.example.map;

import com.crawler.example.entity.ComInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ComInfoMap {

    @Insert("insert into com_info(region,category,name,description,web_url,visit_cnt,from_url,created_time, modified_time) values (#{region},#{category},#{name},#{description},#{web_url},#{visit_cnt},#{from_url},now(),now())")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    int insert(ComInfo comInfo);

    @Select("select id, region,category,name,description,web_url,visit_cnt,from_url, created_time, modified_time from com_info where id = #{id}")
    ComInfo getById(int id);

    @Select("select id, region,category,name,description,web_url,visit_cnt,from_url, created_time, modified_time from com_info where web_url = #{web_url}")
    List<ComInfo> getByUrl(String web_url);

    @Select("select id, region,category,name,description,web_url,visit_cnt,from_url, created_time, modified_time from com_info where category = #{category} and web_url = #{web_url}")
    ComInfo getByCategoryAndUrl(@Param("category") String category, @Param("web_url") String web_url);

    @Select("select c.id,c.region,c.category,c.name,c.description,c.web_url,c.visit_cnt,c.from_url,c.created_time,c.modified_time from com_info c left join msg_requested m on c.id = m.com_info_id where c.category=#{category} and m.id is null limit 0,500")
    List<ComInfo> getMsgUnRequestedListByCategory(@Param("category") String category);

    @Update("update com_info set region=#{region},category=#{category},name=#{name},description=#{description},web_url=#{web_url},visit_cnt=#{visit_cnt},modified_time=now() where id=#{id}")
    void update(ComInfo comInfo);


    @Update("update com_info set visit_cnt=visit_cnt+1,modified_time=now() where id=#{id}")
    void updateVisitCountById(@Param("id") int id);

    @Update("update com_info set visit_cnt=visit_cnt+1,modified_time=now() where category=#{category} and web_url=#{web_url}")
    void updateVisitCountByCategoryAndUrl(@Param("category") String category, @Param("web_url") String web_url);

}

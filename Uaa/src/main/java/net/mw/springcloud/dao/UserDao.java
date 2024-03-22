package net.mw.springcloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.mw.springcloud.pojo.po.UserPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDao extends BaseMapper<UserPO> {
    @Insert("insert into user_role_index(user_id, role_id) values(#{userId}, (select id from role_index where name = #{roleName}));")
    public Integer bindRole(Long userId, String roleName);
    @Select("select * from user_index ")
    public List<UserPO>  getList();
    @Select("select count(*) from user_index ")
    public Integer count();
    @Select("select * from user_index where id = #{id}")
    public UserPO getById(Long id);

    @Select("select * from user_index where username = #{username}")
    public UserPO getByUserName(String username);

    @Select("select count(*) from user_index where username=#{username}")
    public Integer countByUserName(String username);

    @Select("SELECT p.name from permmision_index AS  p,  user_role_index AS ur, role_permmission_index AS rp WHERE  ur.role_id = rp.role_id AND p.id = rp.permmission_id AND ur.user_id= #{id};")
    public List<String> getPermissionById(Long id);
}

package feng.dao;

import feng.model.User;
import feng.model.UserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import feng.dao.IBaseDao;

public interface UserMapper extends IBaseDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int countByExample(UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int deleteByExample(UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    List<User> selectByExampleWithBLOBs(UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    List<User> selectByExample(UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    User selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByExampleWithBLOBs(@Param("record") User record, @Param("example") UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByPrimaryKeyWithBLOBs(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ng_user
     *
     * @mbggenerated Sun Sep 25 08:59:26 CST 2016
     */
    int updateByPrimaryKey(User record);
}
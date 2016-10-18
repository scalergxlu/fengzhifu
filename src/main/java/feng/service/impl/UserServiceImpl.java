package feng.service.impl;
import com.scaler.db.DBManager;
import com.scaler.jwt.Jwt;
import com.scaler.util.DesUtil;
import feng.manager.DaoManager;
import feng.model.Staff;
import feng.model.StaffExample;
import feng.model.User;
import feng.model.UserExample;
import feng.service.IUserService;
import net.minidev.json.JSONObject;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;


@Service("userService")
public class UserServiceImpl implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());


    public List<User> getUserList() {

        List<User> userList = new ArrayList<User>();
        UserExample ue = new UserExample();
        List<User> topicList=new ArrayList<User>();
        QueryRunner qr = DBManager.getInstance().getQueryRunner();

            try {
                 topicList=new ArrayList<User>();

                   String sql ="select * from ng_user where sex= ? order by id desc";
                     Object[] params={0};
                       topicList=qr.query(sql, new BeanListHandler<User>(User.class),params);
                     return topicList;
            } catch (Exception e) {
                this.logger.error("", e);
            }

        return topicList;
//        List<User> luser =  DaoManager.userMapperDao.selectByExample(ue);
////        for(int i=0;i<luser.size();i++){
////
////            User user = new User();
////            user.setId(counter.getAndIncrement());
////            user.setName("abc_"+i);
////            user.setAge(25);
////
////            userList.add(user);
////        }
//
//        return luser;
    }

    public User findUserById(long id) {
        UserExample ue = new UserExample();
        ue.createCriteria().andIdEqualTo(id);

        List<User> luser =  DaoManager.userMapperDao.selectByExample(ue);
        if(luser != null && luser.size() >0){
            return luser.get(0);
        }
        return null;
//        User user = new User();
//        user.setId(id);
//        user.setName("ricky");
//        user.setAge(27);
//
//        return user;
    }

    public User findUserByName(String username) {
        UserExample ue = new UserExample();
        ue.createCriteria().andUser_nameEqualTo(username);

        List<User> luser =  DaoManager.userMapperDao.selectByExample(ue);
        if(luser != null && luser.size() >0){
            return luser.get(0);
        }
        return null;
//        User user = new User();
//        user.setId(counter.getAndIncrement());
//        user.setName(username);
//        user.setAge(25);
//
//        return user;
    }

    public int update(User user) {

        return 1;
    }


    public int deleteById(long id) {
        return 1;
    }

    @Override
    public JSONObject login(String username, String password) {

        JSONObject resultJSON=new JSONObject();
        StaffExample ue = new StaffExample();
        try {
            ue.createCriteria().andAcc_nbrEqualTo(username).andPasswdEqualTo(DesUtil.encrypt(password, DesUtil.PRI_KEY));
        } catch (Exception e) {
            e.printStackTrace();
            resultJSON.put("success", false);
            resultJSON.put("msg", "服务异常请联系管理员");
        }
        List<Staff> userlist =  DaoManager.staffMapper.selectByExample(ue);
        if(userlist != null && userlist.size() >0){
            //校验成功
            //生成token
            //用户名密码校验成功后，生成token返回客户端
            Map<String , Object> payload=new HashMap<String, Object>();
            Date date=new Date();
            payload.put("uid", userlist.get(0).getStaff_id());//用户ID
            payload.put("iat", date.getTime());//生成时间
            payload.put("ext",date.getTime()+1000*60*60);//过期时间1小时
            String token= Jwt.createToken(payload);


            resultJSON.put("success", true);
            resultJSON.put("msg", "登陆成功");
            resultJSON.put("token", token);
         }else{
            resultJSON.put("success", false);
            resultJSON.put("msg", "用户名密码不对");
    }


        return resultJSON;
    }
}

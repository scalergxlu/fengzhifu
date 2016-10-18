package feng.service;
import feng.model.User;
import net.minidev.json.JSONObject;

import java.util.List;
public interface IUserService {
    public JSONObject login(String username, String password);
    public List<User> getUserList();
}

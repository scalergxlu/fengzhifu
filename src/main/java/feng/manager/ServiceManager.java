package feng.manager;

import java.lang.reflect.Field;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import feng.service.IUserService;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class ServiceManager {
	
	
	@Resource
	private IUserService _iUserService;
	public static IUserService iUserService;

	@PostConstruct
	public void init() {
		try {

			Field[] fields = this.getClass().getDeclaredFields();

			for (int i = 0; i < fields.length; i++) {
				String fieldname = fields[i].getName();
				if (fieldname.startsWith("_")) {
					String sfieldname = fieldname.substring(1);
					Field sfield = this.getClass().getDeclaredField(sfieldname);
					sfield.setAccessible(true);
					sfield.set(this, fields[i].get(this));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}

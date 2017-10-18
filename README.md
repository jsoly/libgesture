# 手势密码控件GestureView
由于公司需求而做的控件，感兴趣的可以下载或者fork，懒人可直接依赖使用
![image](https://github.com/jsoly/libgesture/blob/master/img1.jpg)
![image](https://github.com/jsoly/libgesture/blob/master/img2.jpg)
![image](https://github.com/jsoly/libgesture/blob/master/img3.jpg)
## 依赖导入

```
allprojects {
	repositories {
	...
	maven { url 'https://www.jitpack.io' }
	}
}

dependencies {
  	compile 'com.github.jsoly:libgesture:v1.0.0'
}
```
----

## 使用方法
* 布局添加

```

<d.cityaurora.com.libgesture.GestureView
        android:id="@+id/mGestureView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>	
```

### DEMO样例
* MainActivity
------------

```
public class MainActivity extends AppCompatActivity {

    private GestureView mGestureView;
    private String psw1,psw2;
    private String testPassword = "0125";
    private String text = "请绘制解锁图案";
    private int time = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGestureView = (GestureView) findViewById(R.id.mGestureView);
        mGestureView.reset(text);
        mGestureView.addCallback(new GestureView.Callback() {
            @Override
            public void onFinish(String password) {
                if(TextUtils.isEmpty(password))
                    return;
//                setNewPsw(password);
                confirm(password);
            }
        });
    }

    void setNewPsw(String password){
        if(password.length()<4){
            mGestureView.setTextError("至少连接4个点，请重新输入");
            mGestureView.setErrorLine(true);
            mGestureView.resetDelay(text,1000);
        }else {
            if(!TextUtils.isEmpty(psw1)){
                if(!TextUtils.equals(psw1,password)){
                    mGestureView.resetDelay(text,1000);
                    mGestureView.setTextError("两次输入不一致");
                    mGestureView.setErrorLine(true);
                }else {
                    mGestureView.reset("设置成功");
                    //保存密码
                    psw2 = password;
                }
            }else {
                psw1 = password;
                mGestureView.reset("再次确认密码");
            }
        }
    }

    void confirm(String password){
        if(!password.equals(testPassword)){
            time--;
            if(time<=0){
                mGestureView.setTextError("次数用完，密码锁定");
                mGestureView.lock();
                return;
            }
            mGestureView.setTextError("密码错误，剩余验证次数："+time);
            mGestureView.setErrorLine(true);
        }else {
            mGestureView.setText("验证成功");
           
        }
    }
}

```

--------
# end

package com.huawei.codelabs.hiresearch.healthstudy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.codelabs.hiresearch.healthstudy.model.AtrialFibrillationMeasureResultMetadata;
import com.huawei.codelabs.hiresearch.healthstudy.util.ActivityManager;
import com.huawei.hiresearch.bridge.BridgeManager2;
import com.huawei.hiresearch.bridge.model.authentication.UserSessionInfo;
import com.huawei.hiresearch.bridge.model.bridge.JoinInfo;
import com.huawei.hiresearch.bridge.model.response.base.HttpMessageDataResponse;
import com.huawei.hiresearch.bridge.provider.BridgeDataProvider;
import com.huawei.hiresearch.sensorprosdk.datatype.device.SensorProDeviceInfo;
import com.huawei.hiresearch.skin.exceptions.InvalidTaskException;
import com.huawei.hiresearch.skin.listeners.HiResearchAtrialTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchConsentTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchDeviceTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchJoinStudyTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchLoginTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchPermissionTaskListener;
import com.huawei.hiresearch.skin.listeners.HiResearchTaskErrorListener;
import com.huawei.hiresearch.skin.model.atrial.AtrialMeasureResult;
import com.huawei.hiresearch.skin.task.device.HiResearchConnectDeviceTask;
import com.huawei.hiresearch.skin.task.heart.HiResearchAtrialTask;
import com.huawei.hiresearch.skin.task.onboarding.HiResearchConsentTask;
import com.huawei.hiresearch.skin.task.onboarding.HiResearchJoinStudyTask;
import com.huawei.hiresearch.skin.task.onboarding.HiResearchLoginTask;
import com.huawei.hiresearch.skin.task.permission.HiResearchPermissionTask;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity implements HiResearchTaskErrorListener, HiResearchAtrialTaskListener {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.btn_device)
    Button btnDevice;
    @BindView(R.id.btn_measure)
    Button btnMeasure;

    private String TAG = this.getClass().getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);

        //申请授权
        HiResearchPermissionTask permissionActiveTask = new HiResearchPermissionTask(this);
        permissionActiveTask.registerTaskListener(new HiResearchPermissionTaskListener() {
            @Override
            public void onPermissionsDenied() {
                //不同意授权——退出应用
                ActivityManager.getAppManager().exitApp();
            }
        });

        //用户知情同意与隐私申明
        HiResearchConsentTask consentActiveTask = new HiResearchConsentTask(this, disposable);
        consentActiveTask.registerTaskListener(new HiResearchConsentTaskListener() {
            @Override
            public void onAgree() {
                btnLogin.setEnabled(true);
            }

            @Override
            public void onDisAgree() {
                //不同意签署，退出
                ActivityManager.getAppManager().exitApp();
            }
        });

        permissionActiveTask.registerErrorListener(this);
        permissionActiveTask.setNext(consentActiveTask);
        permissionActiveTask.start();
    }

    /**
     * 登录
     *
     * @param view
     */
    @OnClick(R.id.btn_login)
    public void onLoginClicked(View view) {
        //登录
        HiResearchLoginTask loginActiveTask = new HiResearchLoginTask(this, disposable);
        loginActiveTask.registerErrorListener(this);
        loginActiveTask.registerTaskListener(new HiResearchLoginTaskListener() {
            @Override
            public void onLoginSuccess(String projectCode, AuthHuaweiId authHuaweiId, UserSessionInfo hiResearchUserSessionInfo) {
                btnJoin.setEnabled(true);
            }

            @Override
            public void onLoginFailure(String projectCode, HttpMessageDataResponse message) {
                toast("login failed:" + message.toString());
            }
        });
        loginActiveTask.start();
    }

    /**
     * 加入研究项目
     *
     * @param view
     */
    @OnClick(R.id.btn_join)
    public void onJoinClicked(View view) {
        HiResearchJoinStudyTask joinStudyActiveTask = new HiResearchJoinStudyTask(this, disposable);
        joinStudyActiveTask.registerErrorListener(this);
        joinStudyActiveTask.registerTaskListener(new HiResearchJoinStudyTaskListener() {
            @Override
            public void onJoinSuccess(String projectCode, JoinInfo data) {
                btnDevice.setEnabled(true);
            }

            @Override
            public void onJoinFailure(String projectCode, HttpMessageDataResponse message) {
                toast("join study failed:" + message.toString());
            }
        });
        joinStudyActiveTask.start();
    }

    /**
     * 设备连接
     *
     * @param view
     */
    @OnClick(R.id.btn_device)
    public void onDeviceClicked(View view) {
        HiResearchConnectDeviceTask deviceActiveTask = new HiResearchConnectDeviceTask(this, disposable);
        deviceActiveTask.registerErrorListener(this);
        deviceActiveTask.registerTaskListener(new HiResearchDeviceTaskListener() {
            @Override
            public void onDeviceStatusChanged(SensorProDeviceInfo deviceInfo) {
                if (deviceInfo.getDeviceConnectState() == 2) {
                    btnMeasure.setEnabled(true);
                }
            }
        });
        deviceActiveTask.start();
    }

    /**
     * 房颤主动测量
     *
     * @param view
     */
    @OnClick(R.id.btn_measure)
    public void onMeasureClicked(View view) {

    }

    @Override
    public void onHiResearchTaskError(String projectCode, String identifier, InvalidTaskException error) {
        String msg = String.format("Porject:%s;Task:%s;error:%s", projectCode, identifier, error.getMessage());
        toast(msg);
    }

    private void toast(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG);
            }
        });
    }

    /**
     * 房颤测量结果回调
     * @param atrialMeasureResult
     */
    @Override
    public void onAtrialMeasureResult(AtrialMeasureResult atrialMeasureResult) {

    }
}
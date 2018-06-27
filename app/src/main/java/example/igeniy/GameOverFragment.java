package example.igeniy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.vk.sdk.VKScope;

import java.util.List;


import static example.igeniy.StartActivity.typeface;

/**
 * Created by Турал on 08.01.2017.
 */

public class GameOverFragment extends DialogFragment implements DialogInterface.OnClickListener, SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {
    public static boolean CloseDialog = false;
    public static SocialNetworkManager mSocialNetworkManager;
    static boolean newRecord;
    Bitmap resultBitmap;
    Bitmap vkPhoto;
    LinearLayout linearLayout;
    ImageView image;
    Callback callback;
    private View form = null;
    private String Time;
    private OnPostingCompleteListener postingComplete = new OnPostingCompleteListener() {
        @Override
        public void onPostSuccessfully(int socialNetworkID) {
            try {
                Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show(); //выпадает NPE , если до вывода сообщения диалог уже закрыт
            } catch (NullPointerException e) {
            }

        }

        @Override
        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
            Toast.makeText(getActivity(), "Error while sending: " + errorMessage, Toast.LENGTH_LONG).show();
        }
    };
    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = 0;
            switch (view.getId()) {
                case R.id.vk_share:
                    networkId = VkSocialNetwork.ID;
                    ScreenShot();

                    vkPhoto = resultBitmap;


                    break;
            }

            if (true) { // должна быть проверка подключения к сети
                Toast.makeText(getActivity(), "Нет интернет соединения", Toast.LENGTH_LONG).show();
            } else {
                SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
                if (!socialNetwork.isConnected()) {
                    if (networkId != 0) {
                        socialNetwork.requestLogin();
                        //Menu.showProgress("Loading social person");

                        Toast.makeText(getActivity(), "После авторизации нажмите повторно на публикацию", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Bundle postParams = new Bundle();
                    postParams.putString(SocialNetwork.BUNDLE_LINK, "http://habrahabr.ru");
                    postParams.putString(SocialNetwork.BUNDLE_PICTURE, "photo14354205_456239074");

                    socialNetwork.requestPostPhotoMessageLink(resultBitmap, postParams, "qwerty", postingComplete);

                }
            }

        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View form = getActivity().getLayoutInflater()
                .inflate(R.layout.gameoverfragment, null);
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(form);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        //dialog.getWindow().setGravity(Gravity.BOTTOM | Gravity.RIGHT);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Time = bundle.getString("currentTime");
        }
        TextView currentTime = (TextView) form.findViewById(R.id.currentTime);
        TextView NewRecordText = (TextView) form.findViewById(R.id.NewRecordText);
        if (newRecord)
            NewRecordText.setText("НОВЫЙ РЕКОРД");
        currentTime.setText(Time);
        currentTime.setTypeface(typeface);

        Button vk_share = (Button) form.findViewById(R.id.vk_share);
        vk_share.setOnClickListener(loginClick);

        String VK_KEY = getActivity().getString(R.string.vk_app_id);

        String[] vkScope = new String[]{
                VKScope.FRIENDS,
                VKScope.WALL,
                VKScope.PHOTOS,
                VKScope.NOHTTPS,
                VKScope.STATUS,
        };

        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(Menu.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager VkSocialNetwork
            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VK_KEY, vkScope);
            mSocialNetworkManager.addSocialNetwork(vkNetwork);


            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, Menu.SOCIAL_NETWORK_TAG).detach(mSocialNetworkManager).commit(); //Как я понял это Фрагмент в котором отображается оконо социальной сети, если я к ней подключен или подключусь.
            //getFragmentManager().beginTransaction().hide(mSocialNetworkManager).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if (!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }
        image = (ImageView) form.findViewById(R.id.imageView10);
        linearLayout = (LinearLayout) form.findViewById(R.id.linearLayoutResult);

        //resultBitmap = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getHeight(), Bitmap.Config.ARGB_8888);


        return dialog;

    }

    public void registerCallBack(Callback callback) {
        this.callback = callback;
    }

    private void initSocialNetwork(SocialNetwork socialNetwork) {
        if (socialNetwork.isConnected()) {
            switch (socialNetwork.getID()) {
                case VkSocialNetwork.ID:
                    // vk_share.setText("Show VK profile");
                    break;
            }
        }
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }

    @Override
    public void onLoginSuccess(int networkId) {
        Menu.hideProgress();
        // startProfile(networkId);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        Menu.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

        /*EditText loginBox =(EditText)form.findViewById(R.id.login);
        EditText passwordBox=(EditText)form.findViewById(R.id.password);
        String login = loginBox.getText().toString();
        String password = passwordBox.getText().toString();

        TextView loginText = (TextView)getActivity().findViewById(R.id.loginText);
        TextView passwordText = (TextView)getActivity().findViewById(R.id.passwordText);
        loginText.setText(login);
        passwordText.setText(password);*/
    }

    @Override
    public void onDismiss(DialogInterface unused) {

        super.onDismiss(unused);
        CloseDialog = true;
        this.callback.callingBack();

    }

    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

    void ScreenShot() {
        linearLayout.setDrawingCacheEnabled(true);
        linearLayout.buildDrawingCache();
        resultBitmap = Bitmap.createBitmap(linearLayout.getDrawingCache());
        linearLayout.setDrawingCacheEnabled(false);
    }


}

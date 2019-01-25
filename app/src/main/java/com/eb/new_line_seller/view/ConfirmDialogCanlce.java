package com.eb.new_line_seller.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eb.new_line_seller.R;
import com.eb.new_line_seller.util.ToastUtils;


public class ConfirmDialogCanlce extends Dialog {

    private Context context;

    private ClickListenerInterface clickListenerInterface;

    String string, title = "";

    public interface ClickListenerInterface {

        public void doConfirm();

        public void doCancel();
    }

    public ConfirmDialogCanlce(Context context) {
        super(context, R.style.my_dialog);
        this.context = context;

    }

    public ConfirmDialogCanlce(Context context, String str) {
        super(context, R.style.my_dialog);
        this.context = context;
        string = str;
    }

    public ConfirmDialogCanlce(Context context, String str, String title) {
        super(context, R.style.my_dialog);
        this.context = context;
        string = str;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_5, null);
        setContentView(view);

        TextView tv_cancel = view.findViewById(R.id.tv_cancel);
        TextView tv_confirm = view.findViewById(R.id.tv_confirm);
        TextView tv_text = view.findViewById(R.id.tv_text);
        TextView tv_title = view.findViewById(R.id.title);

        tv_text.setText(string);

        if (title.equals(""))
            tv_title.setText("提示信息");
        else
            tv_title.setText(title);


        tv_confirm.setOnClickListener(new clickListener());
        tv_cancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_confirm:
                    clickListenerInterface.doConfirm();
                    break;
                case R.id.tv_cancel:
                    clickListenerInterface.doCancel();
                    break;
            }
        }

    }
}
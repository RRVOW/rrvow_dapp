package com.rrvow.rrchain.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rrvow.rrchain.R;


/**
 * Created by shy on 2017/4/26 0026.
 */

public class BaseDialog extends Dialog {
    private String[] btnNames;
    private OnClickListener[] onClickListeners;
    private int iconRes;
    private String content;
    private String title;
    private Context context;

    public static final int SUBMIT_BTN = 0;
    public static final int CANCEL_BTN = 1;
    private View line;
    private TextView btnCancel;
    private TextView btnSure;
    private View line_one;
    EditText edtInput;

    public interface OnClickListener {
        void onClick(BaseDialog dialog);
    }

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public BaseDialog(Context context, View view) {
        super(context, R.style.dialog_theme);
        setContentView(view);
    }

    public BaseDialog(Context context, String title, String content, String[] btnNames,
                      OnClickListener... onClickListeners) {
        super(context, R.style.dialog_theme);
        this.title = title;
        this.context = context;
        this.content = content;
        this.onClickListeners = onClickListeners;
        this.btnNames = btnNames;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        setContentView(view);
        initView();
    }

    private void initView() {
        TextView titleView = findViewById(R.id.tv_dialog_title);
        TextView contentView = findViewById(R.id.tv_content);
        btnSure = findViewById(R.id.tv_dialog_sure);
        btnCancel = findViewById(R.id.tv_dialog_cancel);
        line = findViewById(R.id.view_line);
        line_one = findViewById(R.id.line_one);
        edtInput = findViewById(R.id.edt_input);
        if (title.length() == 0) {
            title = "确认退出？";
        }
        titleView.setText(title);
        if (content != null && content.length() > 0) {
            contentView.setVisibility(View.VISIBLE);
            contentView.setText(content);
        } else {
            contentView.setVisibility(View.GONE);
        }

        if (null == btnNames) {
            btnCancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            line_one.setVisibility(View.GONE);
            btnSure.setVisibility(View.GONE);
        } else if (null != btnNames && btnNames.length == 1 || onClickListeners == null) {
            btnCancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }

        if (btnNames != null) {
            btnSure.setText(btnNames[SUBMIT_BTN]);
        }

        if (!isOnlyOneButton()) {
            if (onClickListeners != null && onClickListeners.length == 2) {

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListeners[CANCEL_BTN] != null) {
                            onClickListeners[CANCEL_BTN].onClick(BaseDialog.this);
                        }

                    }
                });
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListeners[SUBMIT_BTN] != null) {
                            onClickListeners[SUBMIT_BTN].onClick(BaseDialog.this);
                        }
                    }
                });

            } else if (onClickListeners != null && onClickListeners.length == 1) {
                btnSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListeners[SUBMIT_BTN] != null) {
                            onClickListeners[SUBMIT_BTN].onClick(BaseDialog.this);
                        }
                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

            }

            if (btnNames.length > 1) {
                btnCancel.setText(btnNames[CANCEL_BTN]);
                btnCancel.setVisibility(View.VISIBLE);
                line.setVisibility(View.VISIBLE);
                btnSure.setVisibility(View.VISIBLE);

            }
        } else {
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }


    }

    public void setInputVisibility(int visibility) {
        edtInput.setVisibility(visibility);
    }

    public void setInputHint(String hint) {
        edtInput.setHint(hint);
    }

    public String getInputText() {
        return edtInput.getText().toString();
    }

    public boolean isOnlyOneButton() {
        if (onClickListeners == null || onClickListeners.length == 0) {
            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return true;
        }
        if (onClickListeners.length >= 1) {
            btnCancel.setVisibility(View.VISIBLE);
            return false;
        } else {
            btnCancel.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            return true;
        }
    }
}

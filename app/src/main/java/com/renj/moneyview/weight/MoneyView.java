package com.renj.moneyview.weight;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Toast;

import com.renj.moneyview.R;

/**
 * ======================================================================
 * <p>
 * 作者：Renj
 * <p>
 * 创建时间：2017-12-04   11:42
 * <p>
 * 描述：用于输入金额的EditText
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class MoneyView extends android.support.v7.widget.AppCompatEditText {
    // 默认最大输入的位数，包括小数点
    private static final int DEFAULT_MAX_LENGTH = 11;
    // 默认小数点后保持的位数
    private static final int DEFAULT_POINT_LENGTH = 2;

    private int mMaxLength = DEFAULT_MAX_LENGTH;
    // 当这个值小于等于0时，表示不控制小数点的位数
    private int mPointLength = DEFAULT_POINT_LENGTH;
    // 指定从多少位开始到最后不能是小数点，当小于等于1时表示控制小数点的位置。 点默认最后一位不能是小数点
    private int mPointCannotPosition = DEFAULT_MAX_LENGTH;

    public MoneyView(Context context) {
        this(context, null);
    }

    public MoneyView(Context context, AttributeSet attrs) {
        // 注意：必须指定 AttributeSet 为 R.attr.editTextStyle ，否则控件不能获取焦点
        this(context, attrs, R.attr.editTextStyle);
    }

    public MoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPointLength = 0;
        mPointCannotPosition = 5;
        initView();
    }

    private void initView() {
        // 设置输入类型，注意：这里需要2个同时设置表示输入数字和小数点并且弹出数字键盘
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // 设置监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int currentLength = s.toString().length();

                // 判断最大长度，如果在布局文件中是使用 android:maxLength="11" 属性设置了最大长度，这段代码将不会被执行
                if (currentLength > mMaxLength) {
                    Toast.makeText(getContext(), "最大输入" + mMaxLength + "位数", Toast.LENGTH_SHORT).show();
                    getText().delete(currentLength - 1, currentLength);
                    currentLength = s.toString().length();
                }

                // 判断 mPointCannotPosition 值是否大于1
                if (mPointCannotPosition > 1) {
                    // 判断 当前的长度和是否为不能是小数点的位置并比较当前位置是否为小数点
                    if (currentLength >= mPointCannotPosition && s.charAt(currentLength - 1) == '.') {
                        Toast.makeText(getContext(), "第" + mPointCannotPosition + "位开始不能是小数点", Toast.LENGTH_SHORT).show();
                        getText().delete(currentLength - 1, currentLength);
                        currentLength = s.toString().length();
                    }
                }

                // 判断 mPointLength 值是否大于0
                if (mPointLength > 0) {
                    // 计算小数点后面的位数， 判断 小数点后能保持的最大位数
                    if (s.toString().contains(".") && (s.length() - 1) - (s.toString().indexOf(".")) > mPointLength) {
                        Toast.makeText(getContext(), "小数点后只能保持" + mPointLength + "位", Toast.LENGTH_SHORT).show();
                        getText().delete(currentLength - 1, currentLength);
                        currentLength = s.toString().length();
                    }
                }

                // 如果输入的第一位是点，那么就在前面加一个0
                if (currentLength == 1 && s.charAt(0) == ('.')) {
                    getText().insert(0, "0");
                    currentLength = s.toString().length();
                }

                // 如果第一个输入的是0，那么后面再输入0就仍然是显示0，如果输入大于0的数就用后一个数将0代替，如果输入点就正常输入
                if (s.toString().startsWith("0") && currentLength > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) { // 以0开始并且第二位不是点
                        CharSequence sequence = s.subSequence(1, currentLength);
                        getText().clear();
                        getText().append(sequence);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}

package com.renj.moneyview.weight;

import android.content.Context;
import android.content.res.TypedArray;
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
 * 描述：用于输入金额的EditText<br/><br/>
 * <b>主要功能：</b><br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * 1.指定小数点后能保存的小数位数;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * 2.指定从多少位开始到最后不能为小数点;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * 3.当第一个输入的为小数点时，自动在小数点前面拼接 0，组成 0.的形式;<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;
 * 4.当第一个输入的为0时，如果接着输入0，仍然显示0；如果接着输入大于0的数，就用后面输入的数将0替换.<br/>
 * <p>
 * 修订历史：
 * <p>
 * ======================================================================
 */
public class MoneyView extends android.support.v7.widget.AppCompatEditText {
    // 默认最大输入的位数，包括小数点
    private static final int DEFAULT_MAX_LENGTH = 11;
    // 默认小数点后保持的位数
    private static final int DEFAULT_DECIMAL_LENGTH = 2;

    // 控件中字符的最大长度，包括小数点
    private int mMaxLength = DEFAULT_MAX_LENGTH;
    // 小数点后保持的位数，当小于等于0时，表示不控制小数点的位数
    private int mDecimalLength = DEFAULT_DECIMAL_LENGTH;
    // 指定从多少位开始到最后不能是小数点，当小于等于1时表示控制小数点的位置。 默认最后一位不能是小数点
    private int mPointCannotPosition = DEFAULT_MAX_LENGTH;

    private MoneyChangeListener mMoneyChangeListener;

    public MoneyView(Context context) {
        this(context, null);
    }

    public MoneyView(Context context, AttributeSet attrs) {
        // 注意：必须指定 AttributeSet 为 R.attr.editTextStyle ，否则控件不能获取焦点
        this(context, attrs, R.attr.editTextStyle);
    }

    public MoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initView();
    }

    /**
     * 获取xml文件中定义的属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoneyView);
        mMaxLength = typedArray.getInteger(R.styleable.MoneyView_max_length, DEFAULT_MAX_LENGTH);
        mDecimalLength = typedArray.getInteger(R.styleable.MoneyView_decimal_length, DEFAULT_DECIMAL_LENGTH);
        mPointCannotPosition = typedArray.getInteger(R.styleable.MoneyView_point_cannot_position, DEFAULT_MAX_LENGTH);
    }

    /**
     * 设置监听
     *
     * @param moneyChangeListener
     * @return
     */
    public MoneyView setMoneyChangeListener(MoneyChangeListener moneyChangeListener) {
        this.mMoneyChangeListener = moneyChangeListener;
        return this;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public MoneyView setMaxLength(int maxLength) {
        this.mMaxLength = maxLength;
        return this;
    }

    public int getDecimalLength() {
        return mDecimalLength;
    }

    public MoneyView setDecimalLength(int decimalLength) {
        this.mDecimalLength = decimalLength;
        return this;
    }

    public int getPointCannotPosition() {
        return mPointCannotPosition;
    }

    public MoneyView setPointCannotPosition(int pointCannotPosition) {
        this.mPointCannotPosition = pointCannotPosition;
        return this;
    }

    /**
     * 初始化监听
     */
    private void initView() {
        // 设置输入类型，注意：这里需要2个同时设置表示输入数字和小数点并且弹出数字键盘
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        // 设置监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (mMoneyChangeListener != null)
                    mMoneyChangeListener.beforeTextChanged(s, start, count, after);
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

                // 判断 mDecimalLength 值是否大于0
                if (mDecimalLength > 0) {
                    // 计算小数点后面的位数， 判断 小数点后能保持的最大位数
                    if (s.toString().contains(".") && (s.length() - 1) - (s.toString().indexOf(".")) > mDecimalLength) {
                        Toast.makeText(getContext(), "小数点后只能保持" + mDecimalLength + "位", Toast.LENGTH_SHORT).show();
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

                if (mMoneyChangeListener != null)
                    mMoneyChangeListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mMoneyChangeListener != null)
                    mMoneyChangeListener.afterTextChanged(s);
            }
        });
    }

    /**
     * 输入金额改变监听
     */
    public static abstract class MoneyChangeListener {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
        }
    }
}

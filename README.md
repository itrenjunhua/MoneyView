# *MoneyView*
金额输入框，可以自定义小数点所在的位置和小数点后的小数位数

## 主要功能
> 1.指定小数点后能保存的小数位数;<br/>
> 2.指定从多少位开始到最后不能为小数点;<br/>
> 3.当第一个输入的为小数点时，自动在小数点前面拼接 0，组成 0.的形式;<br/>
> 4.当第一个输入的为0时，如果接着输入0，仍然显示0；如果接着输入大于0的数，就用后面输入的数将0替换.

## 使用
### 1）使用代码指定属性
> ① 布局文件中指定控件
> 
	<com.renj.moneyview.weight.MoneyView
		android:id="@+id/moneyview1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="12dp"
        android:hint="输入金额，代码指定属性"
        android:textColor="@color/colorPrimary" />
> ② 在代码中找到控件并设置属性
> 
	 MoneyView moneyView1 = (MoneyView) findViewById(R.id.moneyview1);
     moneyView1
              .setMaxLength(7)
              .setDecimalLength(3)
              .setPointCannotPosition(5)
              .setMoneyChangeListener(new MoneyView.MoneyChangeListener() {
                 @Override
                 public void afterTextChanged(Editable s) {
                      Log.i("MainActivity", "afterTextChanged => " + s);
                 }
              });

### 2）在布局文件中指定属性
> 直接在布局文件中指定就可以了，不需要在代码里进行指定属性就可以实现功能
> 
	<com.renj.moneyview.weight.MoneyView
        android:id="@+id/moneyview2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="12dp"
        android:hint="输入金额，xml中指定属性"
        android:textColor="@color/colorPrimary"
        app:decimal_length="2"
        app:max_length="9"
        app:point_cannot_position="8" />

## 自定义属性说明
>
	<declare-styleable name="MoneyView">
        <!--设置控件输入内容的最大长度，包含小数点，只有设置了这个属性时，在超出长度之后才会弹出Toast提示，
        如果设置了系统的 android:maxLength 属性，那么在这个属性将失效，超过长度也不会弹出Toast提示。-->
        <attr name="max_length" format="integer" />
        <!--小数点后保持的位数，当小于等于0时，表示不控制小数点的位数-->
        <attr name="decimal_length" format="integer" />
        <!--指定从多少位开始到最后不能是小数点，当小于等于1时表示控制小数点的位置。 默认最后一位不能是小数点-->
        <attr name="point_cannot_position" format="integer" />
    </declare-styleable>

## 动态效果图展示
![金额输入框效果展示](https://raw.githubusercontent.com/itrenjunhua/MoneyView/master/demonstration.gif)

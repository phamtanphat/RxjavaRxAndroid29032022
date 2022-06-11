package com.example.rxjavarxandroid29032022;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    EditText edtInput;
    Button btnAssignColor;
    TextView tvOutput;
    Observable<String> stringObservable;
    int color;
    Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtInput = findViewById(R.id.edit_text);
        btnAssignColor = findViewById(R.id.button_assign_color);
        tvOutput = findViewById(R.id.text_view_output);

        random = new Random();

        btnAssignColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edtInput.getText().toString();

                if (!input.isEmpty()) {
                    stringObservable = Observable.just(input);
                    stringObservable
                            .subscribeOn(Schedulers.io())
                            .map(new Function<String, Spanned>() {
                                @Override
                                public Spanned apply(String s) throws Throwable {
                                    color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        return Html.fromHtml("<font color=" + color + ">" + s + "</font>", Html.FROM_HTML_MODE_LEGACY);
                                    } else {
                                        return Html.fromHtml("<font color=" + color + ">" + s + "</font>");
                                    }
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(observer);
                }
            }
        });
    }

    private final Observer<Spanned> observer = new Observer<Spanned>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {

        }

        @Override
        public void onNext(@NonNull Spanned spanned) {
            tvOutput.setText(spanned);
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
}

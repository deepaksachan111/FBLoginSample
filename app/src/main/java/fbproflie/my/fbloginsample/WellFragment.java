package fbproflie.my.fbloginsample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class WellFragment extends android.support.v4.app.Fragment {


    public WellFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_well, container, false);

        ImageView imageView =(ImageView)v.findViewById(R.id.iv_pro);
        TextView tt =(TextView)v.findViewById(R.id.ttttttttttttt);

           String s = getArguments().getString("tt");
           tt.setText(s);

           Bitmap bitmap = getArguments().getParcelable("bitmap");

        imageView.setImageBitmap(bitmap);
        return v;
    }




}

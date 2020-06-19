package  com.qboxus.hugme.Chat_pkg.Videos;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import  com.qboxus.hugme.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Play_Video_F extends Fragment {


    private VideoView vv;
    private MediaController mediacontroller;
    private Uri uri;

    View view;
    Context context;
    public Play_Video_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_play_video, container, false);
        context=getContext();

        vv = (VideoView) view.findViewById(R.id.videoview);

        mediacontroller = new MediaController(context);
        mediacontroller.setAnchorView(vv);
        String filepath=getArguments().getString("path");
        uri = Uri.parse(filepath);

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                getActivity().onBackPressed();
            }
        });

        vv.setMediaController(mediacontroller);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();

        return view;
    }

}

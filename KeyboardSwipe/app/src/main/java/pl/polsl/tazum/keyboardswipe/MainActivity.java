package pl.polsl.tazum.keyboardswipe;

import android.app.Activity;
        import android.support.v4.view.GestureDetectorCompat;
        import android.os.Bundle;
        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.widget.TextView;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener{

    private GestureDetectorCompat mDetector;
    char first = 'A';       //first character in range of scanning
    char last = 'Z';        //last character in range of scanning
    boolean space = true;   //is space in range of scanning

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this,this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        //Detect common gestures
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float vX, float vY) {
        if(Math.abs(vX) > Math.abs(vY)){
            //Horizontal fling
            if(vX<0){
                //Left fling, narrow scanning range and exclude space
                last = (char)((last-first+(space?1:0))/2+first);
                space = false;
            } else {
                //Right fling, narrow scanning range
                first = (char)((last-first+(space?1:0))/2+first+1);
            }
            updateLetters();
        } else {
            //Vertical fling
            if(vY>0 || first <last) {
                //Down fling or no character selected, reset scanning
                reset();
            } else {
                //Up fling and character selected, confirm character
                confirm(space?' ':first);
            }
        }
        return true;
    }

    private void confirm(char c) {
        ((TextView) findViewById(R.id.enteredText)).setText(((TextView) findViewById(R.id.enteredText)).getText().toString()+c);
        reset();
    }

    private void updateLetters() {
        if(last <= first){
            //Single character in range of scanning, treat it as selected
            ((TextView) findViewById(R.id.leftLetters)).setText("");
            ((TextView) findViewById(R.id.rightLetters)).setText("");
            ((TextView) findViewById(R.id.mainLetter)).setText(space?"SPACE":Character.toString(first));
        } else {
            //Divide characters to left and right side
            ((TextView) findViewById(R.id.leftLetters)).setText(prepareLetters(first, (char) ((last - first + (space ? 1 : 0)) / 2 + first)));
            String right = prepareLetters((char) ((last - first + (space ? 1 : 0)) / 2 + first + 1), last);
            if (space) {
                right += "\nSPACE";
            }
            ((TextView) findViewById(R.id.rightLetters)).setText(right);
        }
    }

    private String prepareLetters(char first, char last){
        //Prepare string consisting of ordered letters in given range
        String letters = "";
        int length = 0;
        for(char i = first; i<=last;i++){
            if(length>=5){
                length=0;
                letters+="\n";
            }
            letters += i + " ";
            length++;
        }
        return letters;
    }

    private void reset() {
        //Reset scanning
        first = 'A';
        last = 'Z';
        space = true;
        ((TextView) findViewById(R.id.mainLetter)).setText("");
        updateLetters();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}

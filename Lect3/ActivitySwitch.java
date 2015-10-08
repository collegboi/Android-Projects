public Class RedScreenActivity extends Activity {
    
    
    Button greenButton;
    
    public void onCreate(Bundle status) {
    
        super.onCreate...;
        setContentView(R.layout....);
        greenButton = (Button)findViewByID(R.id.....);
        greenButton.setOnClickListener(.....);
            
    }s
    public void onClick(View v) {
                Intent switchScreen = new Intent(RedScreenActivity.this, GreenScreen.class);
                //put extra for segue passing values;
                startActivity(switchScreen);
            );

    }

}

public Class GreenScreenActiviy extends Activity {
    
    RedButton button;
    
    public void onCreate(Bundle status) {
        
        
    }
    
    public void onClick(View v) {
        
        finish();

    }
}
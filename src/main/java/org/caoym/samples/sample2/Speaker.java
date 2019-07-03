package org.caoym.samples.sample2;

class Speaker implements SpeakerInterface {
    private String hello = "";

    Speaker(String hello){
        this.hello = hello;
    }

    public void helloTo(String somebody) {
        System.out.println(this.hello +" "+ somebody);
        //System.out.println(this.hello);
        //System.out.println(somebody);
    }
}

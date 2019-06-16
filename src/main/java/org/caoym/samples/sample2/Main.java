package org.caoym.samples.sample2;

public class Main{

    private final static SpeakerInterface speaker = new Speaker("Hello");

    public static void main(String[] args){
	System.out.println("arg length : "+ args.length);
        speaker.helloTo(args[0]);
    }
}

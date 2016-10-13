package com.sslcs.uniform.net.bean.response;

import com.sslcs.uniform.net.bean.Movie;

import java.util.ArrayList;

public class Top250Response {
    public int count;
    public int start;
    public int total;
    public ArrayList<Movie> subjects;
    public String title;
}

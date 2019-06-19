package com.githup.yafeiwang1240;

import com.githup.yafeiwang1240.obrien.uitls.RegexUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        System.out.println( RegexUtils.getValue("hello world, my", ",") );
    }
}

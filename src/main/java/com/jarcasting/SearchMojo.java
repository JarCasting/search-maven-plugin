/*
 * Copyright (c) 2020 JarCasting
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jarcasting;




import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;


/**
 * Goal which search maven artifacts
 */
@Mojo( name = "search", requiresProject = false)
public class SearchMojo
    extends AbstractMojo
{

    @Parameter(property = "q", defaultValue = "", required = true, readonly = true)
    public String query;


   public void execute()
        throws MojoExecutionException
    {

        try {
            URL url = new URL("https://jarcasting.com/search/?q="+ URLEncoder.encode(query, "UTF-8"));
            //System.out.println(url.toString());
            URLConnection connection = url.openConnection();
            connection.addRequestProperty( "Cache-control", "no-cache");
            connection.addRequestProperty( "Cache-store", "no-store");
            connection.addRequestProperty( "Pragma", "no-cache");
            connection.addRequestProperty( "Accept", "text/plain");
            connection.addRequestProperty( "User-Agent", "com.jarcasting/search-maven-plugin");


            Reader reader = null;
            if ("gzip".equals(connection.getContentEncoding())) {
                reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()));
            }
            else {
                reader = new InputStreamReader(connection.getInputStream());
            }

            while (true) {
                int ch = reader.read();
                if (ch==-1) {
                    break;
                }
                System.out.print((char)ch);
            }


        } catch (IOException e) {

        }

    }
}

package com.reflectoring.io.java15;

public class TextBlocks {
    public static void main(String[] args) {
        System.out.println(
        "<!DOCTYPE html>\n" +
                "<html>\n" +
                "     <head>\n" +
                "        <title>Example</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <p>This is an example of a simple HTML " +
                "page with one paragraph.</p>\n" +
                "    </body>\n" +
                "</html>\n");

        System.out.println(
        """
                <!DOCTYPE html>
                <html>
                    <head>
                        <title>Example</title>
                    </head>
                    <body>
                        <p>This is an example of a simple HTML 
                        page with one paragraph.</p>
                    </body>
                </html>      
                """
        );
    }
}

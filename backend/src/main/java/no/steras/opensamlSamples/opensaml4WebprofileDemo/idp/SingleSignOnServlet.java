package no.steras.opensamlSamples.opensaml4WebprofileDemo.idp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import no.steras.opensamlSamples.opensaml4WebprofileDemo.sp.SPConstants;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * シングルサインオンサーブレット
 */
public class SingleSignOnServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(SingleSignOnServlet.class);

    /**
     * GETメソッド処理
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("SingleSignOnServlet.doGet: SPからAuthnRequestを受信しました。");
        Writer w = resp.getWriter();
        resp.setContentType("text/html");
        w.append("<html>" + "<head></head>" + "<body><h1>You are now at IDP, click the button to authenticate</h1> <form method=\"POST\">"
                + "<input type=\"submit\" value=\"Authenticate\"/>" + "</form>" + "</body>" + "</html>");
    }

    /**
     * POSTメソッド処理
     * SAMLリクエスト受信後の認証処理完了後に呼び出される想定
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info("SingleSignOnServlet.doPost: 認証ボタンがクリックされました。SAMLアーティファクトを生成します。");
        String redirectUrl = SPConstants.ASSERTION_CONSUMER_SERVICE + "?SAMLart=AAQAAMFbLinlXaCM%2BFIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg%3D";
        logger.info("ユーザーが認証されました。アーティファクトを含めてSPにリダイレクトします: " + redirectUrl);
        // 実際の認証処理は省略し、認証成功したものとしてSAMLアーティファクトをSPに送信する
        resp.sendRedirect(redirectUrl);
    }


}

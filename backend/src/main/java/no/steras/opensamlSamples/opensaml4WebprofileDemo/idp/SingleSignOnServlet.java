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
 * Identity Provider (IdP) 側の Single Sign-On (SSO) エンドポイント。
 * 
 * 役割:
 * 1. ユーザー（ブラウザ）からの認証リクエストを受信。
 * 2. ユーザーを認証（このデモでは単純なボタンクリックで代用）。
 * 3. 認証成功後、SAML アーティファクトを生成して SP へリダイレクト。
 */
public class SingleSignOnServlet extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(SingleSignOnServlet.class);

    /**
     * 認証ページを表示します。
     * 実際にはここで ID/パスワード入力画面などが表示されます。
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("SingleSignOnServlet: SP から AuthnRequest を受信しました。認証画面を表示します。");
        Writer w = resp.getWriter();
        resp.setContentType("text/html");
        w.append("<html>" + "<head><title>IdP Login</title></head>" 
                + "<body>"
                + "<h1>OpenSAML Demo IdP</h1>"
                + "<p>あなたは現在 IdP にリダイレクトされています。下のボタンを押して「認証」を完了してください。</p>"
                + "<form method=\"POST\">"
                + "<input type=\"submit\" value=\"Authenticate / 認証完了\"/>" 
                + "</form>" 
                + "</body>" + "</html>");
    }

    /**
     * ユーザーが認証に成功したとみなし、SAML アーティファクトを発行して SP へ戻します。
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        logger.info("SingleSignOnServlet: 認証が成功しました。SAML アーティファクトを発行します。");

        // 本来はランダムなアーティファクトを動的に生成し、セッション/DBに保存しますが、
        // このデモでは固定のアーティファクト値を使用して SP の ACS URL へリダイレクトします。
        // SP 側はこの値をキーにして、後ほどバックチャネルで問い合わせに来ます。
        String artifactValue = "AAQAAMFbLinlXaCM%2BFIxiDwGOLAy2T71gbpO7ZhNzAgEANlB90ECfpNEVLg%3D";
        String redirectUrl = SPConstants.ASSERTION_CONSUMER_SERVICE + "?SAMLart=" + artifactValue;
        
        logger.info("ユーザーを SP の ACS ({}) へ送り返します。", SPConstants.ASSERTION_CONSUMER_SERVICE);
        resp.sendRedirect(redirectUrl);
    }
}

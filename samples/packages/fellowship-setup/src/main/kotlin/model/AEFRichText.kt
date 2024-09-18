/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package model

import Fellowship
import com.atlan.Atlan

object AEFRichText {
    fun getPlainTextEmail(scholar: Fellowship.Scholar): String {
        val client = Atlan.getDefaultClient()
        return """
            Hi ${scholar.firstName},

            Welcome to the Atlan Engineering Fellowship! Below you will find the key details to access the Atlan tenant where we will be running hands-on exercises in Atlan.

            NOTE: THESE ARE SPECIFIC TO YOU, SO PLEASE KEEP THEM SAFE!

            - Tenant URL: ${client.baseUrl}
            - Your unique ID: ${scholar.id}
            - Your API token (should be treated as a single line, ignoring the -------'s):

            -------
            ${Fellowship.apiTokens[scholar.id]!!.attributes.accessToken}
            -------

            We have created an isolated area, protected by these details so that only you can access it. All of the isolated assets will be part of this connection:

            ${client.baseUrl}/assets/${Fellowship.dbConnections[scholar.id]!!.guid}/overview

            I hope you're as excited about the week ahead as we are!

            Cheers,
            The Atlan Team
            """.trimIndent()
    }

    fun getHTMLEmail(scholar: Fellowship.Scholar): String {
        val client = Atlan.getDefaultClient()
        return """
            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml" xmlns="http://www.w3.org/1999/xhtml" lang="en">

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                <meta content="telephone=no" name="format-detection" />
                <title></title>
                <!--[if mso]>
                  <style type="text/css">
                    body, table, td {font-family: Arial, Helvetica, sans-serif !important;}
                  </style>
                <![endif]-->
            <style>body {
            margin: 0; padding: 0; background-color: #f8f8fd; font-size: 15px; line-height: 1.6; color: #444444; width: 100%;
            }
            body {
            font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif;
            }
            img {
            border: 0 none; line-height: 100%; outline: none; text-decoration: none; vertical-align: baseline; font-size: 0;
            }
            a:hover {
            text-decoration: underline;
            }
            .btn:hover {
            text-decoration: none;
            }
            .btn.bg-secondary:hover {
            background-color: #ecf0f2 !important;
            }
            .btn.bg-bordered:hover {
            background-color: #f9fbfe !important;
            }
            a.bg-blue:hover {
            background-color: #3a77cc !important;
            }
            a.bg-azure:hover {
            background-color: #37a3f1 !important;
            }
            a.bg-indigo:hover {
            background-color: #596ac9 !important;
            }
            a.bg-purple:hover {
            background-color: #9d50e8 !important;
            }
            a.bg-pink:hover {
            background-color: #f55f91 !important;
            }
            a.bg-red:hover {
            background-color: #c01e1d !important;
            }
            a.bg-orange:hover {
            background-color: #fd8e35 !important;
            }
            a.bg-yellow:hover {
            background-color: #e3b90d !important;
            }
            a.bg-lime:hover {
            background-color: #73cb2d !important;
            }
            a.bg-green:hover {
            border-color: #426ddf !important;
            background-color: #426ddf !important;
            }
            a.bg-teal:hover {
            background-color: #28beae !important;
            }
            a.bg-cyan:hover {
            background-color: #1596aa !important;
            }
            a.bg-gray:hover {
            background-color: #95a9b0 !important;
            }
            .img-hover:hover img {
            opacity: .64;
            }
            @media only screen and (max-width: 560px) {
              body {
                font-size: 14px !important;
              }
              body {
                font-size: 14px;
              }
              .content {
                padding: 24px !important;
              }
              .content-image-text {
                padding: 24px !important;
              }
              .content-image {
                height: 100px !important;
              }
              .content-image-text {
                padding-top: 96px !important;
              }
              h1 {
                font-size: 24px !important;
              }
              .h1 {
                font-size: 24px !important;
              }
              h2 {
                font-size: 20px !important;
              }
              .h2 {
                font-size: 20px !important;
              }
              h3 {
                font-size: 18px !important;
              }
              .h3 {
                font-size: 18px !important;
              }
              .col {
                display: table !important; width: 100% !important;
              }
              .col-spacer {
                display: table !important; width: 100% !important;
              }
              .col-spacer-xs {
                display: table !important; width: 100% !important;
              }
              .col-spacer-sm {
                display: table !important; width: 100% !important;
              }
              .col-hr {
                display: table !important; width: 100% !important;
              }
              .row {
                display: table !important; width: 100% !important;
              }
              .col-hr {
                border: 0 !important; height: 24px !important; width: auto !important; background: transparent !important;
              }
              .col-spacer {
                width: 100% !important; height: 24px !important;
              }
              .col-spacer-sm {
                height: 16px !important;
              }
              .col-spacer-xs {
                height: 8px !important;
              }
              .chart-cell-spacer {
                width: 4px !important;
              }
              .text-mobile-center {
                text-align: center !important;
              }
              .d-mobile-none {
                display: none !important;
              }
            }
            @media screen and (max-width: 600px) {
              u+.body {
                width: 100vw !important;
              }
            }

            </style></head>
            <body class="bg-body" style="font-size: 15px; line-height: 1.6; color: #444444; width: 100%; font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; margin: 0; padding: 0;" bgcolor="#f8f8fd">

            <center>
            <table class="main bg-body" width="100%" cellspacing="0" cellpadding="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%;" bgcolor="#f8f8fd">
              <tr>
                <td align="center" valign="top" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif;">
                <!--[if (gte mso 9)|(IE)]>
                <table border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="center" valign="top" width="640">
                <![endif]-->
                  <table class="wrap" cellspacing="0" cellpadding="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%; max-width: 640px; text-align: left;">
                    <tr>
                      <td class="p-sm" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 8px;">
                        <table cellpadding="0" cellspacing="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%;">
                          <tr>
                            <td class="py-sm" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding-top: 8px !important; padding-bottom: 8px !important;">
                            </td>
                          </tr>
                        </table>
                        <div class="main-content">
                          <table class="box" cellpadding="0" cellspacing="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%; border-radius: 2px; -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05); box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05); border: 1px solid #f0f0f0;" bgcolor="#ffffff">
                            <tr>
                              <td style="font-size:6px; line-height:10px; padding:32px 0px 32px 0px;" valign="top" align="center">
                                <img class="max-width" border="0" style="display:block; color:#000000; text-decoration:none; font-family:Helvetica, arial, sans-serif; font-size:16px;" width="100" alt="" data-proportionally-constrained="true" data-responsive="false" src="http://cdn.mcauto-images-production.sendgrid.net/7ee1a9303c19cddf/dc7c16b9-feb1-4e14-9777-c9f5c40fd921/100x31.png" height="31">
                              </td>
                            </tr>
                            <tr>
                              <td style="font-size:6px; line-height:10px; padding:0px 0px 0px 0px;" valign="top" align="center">
                                <img class="max-width" border="0" style="display:block; color:#000000; text-decoration:none; font-family:Helvetica, arial, sans-serif; margin-top:15px; font-size:16px; max-width:100% !important; width: 360; height:auto !important;" width="360" alt="" data-proportionally-constrained="true" data-responsive="true" src="https://marketing-image-production.s3.amazonaws.com/uploads/9b72d8ab1326e09a6e371f8ea0fbf6fe484759d0c2640e812e644e938550af994993e117ee9702707afabfd1cf0ad48b2a5f5b46ac050555406d038e29049fbc.png">
                              </td>
                            </tr>
                            <tr>
                              <td style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif;">
                                <table cellpadding="0" cellspacing="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%;">
                                  <tr>
                                    <td class="content text-center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 40px 48px 10px 48px;" align="center">
                                      <p class="h1" style="font-weight: 600; font-size: 20px; line-height: 1.3; margin: 0 0 .5em;">Hi ${scholar.firstName} &#8212; welcome to the Atlan Engineering Fellowship!</p>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content text-center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 0px 48px 20px 48px;" align="center">
                                      <p class="h1" style="font-weight: 400; font-size: 16px; line-height: 1.3; margin: 0 0 .5em;">Below you will find the key details you need to access Atlan.<br/><strong>NOTE: THESE ARE SPECIFIC TO YOU, SO PLEASE KEEP THEM SAFE!</strong></p>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content text-center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 0px 48px 20px 48px;" align="center">
                                      <p class="h1" style="font-weight: 400; font-size: 16px; line-height: 1.3; margin: 0 0 .5em;">
                                        Tenant URL: <a href="${client.baseUrl}">${client.baseUrl.substringAfter("https://")}</a>
                                        <br/>
                                        Your unique ID: <strong>${scholar.id}</strong>
                                      </p>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content text-center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 0px 48px 20px 48px;" align="center">
                                      <p class="h1" style="font-weight: 400; font-size: 16px; line-height: 1.3; margin: 0 0 .5em;">
                                        To access the tenant programmatically, use the API token attached.
                                      </p>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content text-center pt-0 pb-xl" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 0 48px 10px;" align="center">
                                      <table cellspacing="0" cellpadding="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%;">
                                        <tr>
                                          <td align="center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif;">
                                            <table cellpadding="0" cellspacing="0" border="0" class="bg-green rounded w-auto" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: separate; width: auto; color: #ffffff; border-radius: 2px;" bgcolor="#5eba00">
                                              <tr>
                                                <td align="center" valign="top" bgcolor="#2026d2" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif;" >
                                                <a href="${client.baseUrl}/assets/${Fellowship.dbConnections[scholar.id]!!.guid}/overview" style="background-color: #2026d2; border: 0px solid #2026d2; border-color:#2026D2; color: #ffffff; display:inline-block; font-size: 16px; font-weight: bold; letter-spacing:0px; line-height: normal;  padding: 12px 32px; text-align:center; text-decoration: none; -webkit-transition: .3s background-color; transition: .3s background-color;">
                                                  <span class="btn-span" font-color="#fff" style="color: #ffffff; font-size: 16px; text-decoration: none; font-weight: 600; line-height: 20px;">Your Unique Connection</span>
                                                </a>
                                                </td>
                                              </tr>
                                            </table>
                                          </td>
                                        </tr>
                                      </table>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                  <tr>
                                    <td class="content text-center" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding: 0px 48px 20px 48px;" align="center">
                                      <p class="h1" style="font-weight: 400; font-size: 16px; line-height: 1.3; margin: 0 0 .5em;">
                                        We hope you're as excited about the week ahead as we are!<br/>So happy you're here. 💙
                                      </p>
                                      <p style="margin: 0 0 1em;"></p>
                                    </td>
                                  </tr>
                                </table>
                              </td>
                            </tr>
                          </table>
                        </div>
                        <table cellspacing="0" cellpadding="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%;">
                          <tr>
                            <td class="py-sm" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; padding-top: 8px !important; padding-bottom: 8px !important;">
                              <table class="font-sm text-center text-muted" cellspacing="0" cellpadding="0" style="font-family: Open Sans, -apple-system, BlinkMacSystemFont, Roboto, Helvetica Neue, Helvetica, Arial, sans-serif; border-collapse: collapse; width: 100%; color: #9eb0b7; text-align: center; font-size: 13px;">
                              </table>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                  <!--[if (gte mso 9)|(IE)]>
                      </td>
                    </tr>
                  </table>
                  <![endif]-->
                </td>
              </tr>
            </table>
            </center>
            </body>
            </html>
            """.trimIndent()
    }

    fun getConnectionReadme(): String {
        return """
            <h1>Welcome to the Atlan Engineering Fellowship! 🎓</h1>
            <p>Why have we brought you to this page?</p><p></p>
            <p>This <a target="_blank" rel="noopener noreferrer nofollow" href="https://developer.atlan.com/concepts/review/#connections">connection</a> is the root of all <em>your</em> assets in Atlan. You, and only you, will have complete control over all the assets in it. So no need to worry about anyone else messing about with your metadata!</p><p></p>
            <p>Some useful points of reference for your journey:</p>
            <ul>
                <li><p><a target="_blank" rel="noopener noreferrer nofollow" href="https://developer.atlan.com/getting-started/intro/">Getting started</a> with extending Atlan</p></li>
            </ul>
            <p>Looking forward to all the amazing stuff you'll do this week! 💙</p>
            """.trimIndent()
    }
}

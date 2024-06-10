package Files;


import StepDefination.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import Classes.*;
public class payload {

    public static String BaseURL() {
        return "https://sales-backend-test.spinnyworks.in";
    }

//    public static String cookie() {
//
//        return "csrftoken=iCfmd0nGcoPwmGoH2Nf6itOFGJYqa59hua9HgS2CZfhzHPujR2hliiTKpE6KRbmy; E6DY9k9eVsIIv8AwVELuOQ|1663850239|e30|t_-qN7_KhST9HTnLzkQXympzeqg; rl_page_init_referrer=RudderEncrypt:U2FsdGVkX1+rm4JKR6C/sWjWqwjkyIPtBR+b/9nzdWk=; rl_page_init_referring_domain=RudderEncrypt:U2FsdGVkX1+tieGKLrHmM84dEKkF9Gs3NB+IjfxFiAo=; _gcl_au=1.1.1211563789.1665485501; _fbp=fb.1.1665485502612.64453271; _hjSessionUser_1522085=eyJpZCI6IjQ2NzU4NGIzLTg0OWEtNTA5Ny1iYzkwLTBlMDE4ZjU5YjI4NyIsImNyZWF0ZWQiOjE2NjU0ODU1NDY5MjMsImV4aXN0aW5nIjp0cnVlfQ==; utm_source=direct; varnishPrefixHome=true; _ga=GA1.2.2007121561.1665485409; _ga_WQREN8TJ7R=GS1.1.1665493180.1.1.1665493515.60.0.0; _clck=11wluj3|1|f5o|0; rl_user_id=RudderEncrypt:U2FsdGVkX19ZuDd4ZhBj2KQBCrSVTVrGH5cxyn5mKriqJFHtikKwPyQhspXMBBFUEWsl30hrh7eJrCL2CcwsSA==; rl_anonymous_id=RudderEncrypt:U2FsdGVkX19Mqo8fUACf4E9LnzVCZv8CpMd+e1cSCg7TFTPjW8gs27x7wW8Sl2UW1zy/iBm4/J2yTfZJYKyIog==; rl_group_id=RudderEncrypt:U2FsdGVkX19CdbWBO807cSO95HilDKJfzt09s+yfSUw=; rl_trait=RudderEncrypt:U2FsdGVkX18q8BzUt8rzaOty5/n7m8yrhayw418k6ds=; rl_group_trait=RudderEncrypt:U2FsdGVkX1+wjgMuxDMbWlGjPPJ6wJ9FCoCbG6S+2X0=; rl_session=RudderEncrypt:U2FsdGVkX19H6m0NO+ZGg5wgtZqzmMHnqyVVA+S1fj9u5g1gepT5p9EKc4+3lsoDqRBLoC9/feLTbWyGEcivKuHmRgXhUgRazAYfkf7qM5thfHCEnocGLSgUjPkIRghwvrB/TrbVbSen+ZY1X5/03g==; _uetvid=b085b150495211edac5867c9641216f1; _gid=GA1.2.2119205379.1665926111; session_state=54af5ff7-03cd-4eff-bef1-89b8049efc44; keycloak_session=MT-NoG2ihFgj-c3dyzOx7g|1666184438|llIMoGWMmUTPx0jnetyNUY_Sics|06007c88-06f0-49b4-b967-d667fc9926c3|132a1ac0-4ae5-43be-aba6-640e0dbd9ff2; _gat_gtag_UA_61804048_15=1; _hjSession_1522085=eyJpZCI6IjkyYzlkNDgwLTI2YWMtNDVkMy04YzU0LTMzY2E5YThhYzU0YSIsImNyZWF0ZWQiOjE2NjYxODE0OTc4NDAsImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=0; _gat=1";
//    }


    public static String BuyLeadCreate(String phoneNumber, String category) {
        return "{\r\n"
                + "  \"contact_details\": {\r\n"
                + "   \"contact_number\": \"" + phoneNumber + "\",\r\n"
                + "    \"name\": \"QA Automation\",\r\n"
                + "    \"address_line_1\": \"Noida\",\r\n"
                + "    \"address\": \"Noida, Uttar Pradesh, India\",\r\n"
                + "    \"lat\": 28.5355161,\r\n"
                + "    \"long\": 77.3910265,\r\n"
                + "    \"city_name\": \"Noida\"\r\n"
                + "  },\r\n"
                + "  \"category\": \"" + category + "\",\r\n"
                + "  \"lead_city\": \"delhi-ncr\",\r\n"
                + "  \"source\": {\r\n"
                + "    \"context_type\": \"webarticle\",\r\n"
                + "    \"id\": 7526\r\n"
                + "  },\r\n"
                + "  \"sub_source\": \"\"\r\n"
                + "}";


    }

    public static String BuyLeadCreateMultipleSource(String phoneNumber, String category, String source) {
        return "{\r\n"
                + "  \"contact_details\": {\r\n"
                + "   \"contact_number\": \"" + phoneNumber + "\",\r\n"
                + "    \"name\": \"Kavya Automation\",\r\n"
                + "    \"address_line_1\": \"Noida\",\r\n"
                + "    \"address\": \"Noida, Uttar Pradesh, India\",\r\n"
                + "    \"lat\": 28.5355161,\r\n"
                + "    \"long\": 77.3910265,\r\n"
                + "    \"city_name\": \"Noida\"\r\n"
                + "  },\r\n"
                + "  \"category\": \"" + category + "\",\r\n"
                + "  \"lead_city\": \"delhi-ncr\",\r\n"
                + "  \"source\": {\r\n"
                + "    \"context_type\": \"webarticle\",\r\n"
                + "    \"id\": " + source + "\r\n"
                + "  },\r\n"
                + "  \"sub_source\": \"\"\r\n"
                + "}"
                ;


    }

    public static String addBuyerAddress() {
        return "{\r\n"
                + "  \"pin_code\": \"201309\",\r\n"
                + "  \"city\": \"Noida\",\r\n"
                + "  \"state\": \"Uttar Pradesh\",\r\n"
                + "  \"address_line_1\": \"noida\",\r\n"
                + "  \"landmark\": \"\",\r\n"
                + "  \"email\": \"" + DealCreate.buyerEmail + "\",\r\n"
                + "  \"name\": \"Kavyassassas\",\r\n"
                + "  \"category\": \"" + createLead.leadCategory + "\",\r\n"
                + "  \"address_type\": \"new_address_on_rc\",\r\n"
                + "  \"parent_address_type\": null,\r\n"
                + "  \"care_of_name\": null,\r\n"
                + "  \"contact_number\": \"" + createLead.phoneNumber + "\",\r\n"
                + "  \"date_of_birth\": null,\r\n"
                + "  \"gstin_no\": null,\r\n"
                + "  \"pan_number\": \"\",\r\n"
                + "  \"gender\": \"\",\r\n"
                + "  \"marital_status\": \"\"\r\n"
                + "}";
    }

    public static String addingBuyer(String phoneNumber) {
        return "{\r\n"
                + "  \"buy_lead\": " + createLead.leadId + ",\r\n"
                + "  \"buyers\": [\r\n"
                + "    {\r\n"
                + "      \"contact_number\": \"" + phoneNumber + "\",\r\n"
                + "      \"name\": \"Kaaavya\",\r\n"
                + "      \"action\": \"create\"\r\n"
                + "    }\r\n"
                + "  ]\r\n"
                + "}";
    }
//    public static String CancelDeal() {
//        return "{\"status\":{\"name\":\"deal-flow-deal-flow-deal-cancelled\",\"description\":\"Deal Cancelled\"},\"reason\":\"Customer not responding\",\"comment\":\"mnn\"}";
//    }

    public static String switchBuyer(String buyerID)
    {
    	return "{\"operationName\":\"switchBuyer\",\"variables\":{\"buyerId\":"+buyerID+",\"dealId\":"+DealCreate.dealId+"},\"query\":\"mutation switchBuyer($buyerId: Int!, $dealId: Int) {\\n  switchBuyer(buyerId: $buyerId, dealId: $dealId) {\\n    ok\\n    buyer {\\n      pk\\n      __typename\\n    }\\n    loanApplication {\\n      pk\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}";
    }
    
    public static String addingCarToInterest() {
        return "[\r\n"
        		+ "  {\r\n"
        		+ "    \"buy_lead\": \""+createLead.leadId+"\",\r\n"
        		+ "    \"lead_id\": "+addCars.sell_lead_id+"\r\n"
        		+ "  }\r\n"
        		+ "]";
    }

    public static String TokenPayment(int tokenAmount) {
        return "{\"item\":\"Token\",\"mode\":\"Cash\",\"extra_fees\":0,\"amount\":"+tokenAmount+",\"transaction_id\":null,\"paid_amount\":"+tokenAmount+",\"allow_excess\":false}";
    }

    public static String PartialPayment(String paymentType, double fees) {
        double percentAmount = 5000 * (fees / 100);
        double totalAmount = percentAmount + 5000;

        return "{\"item\":\"Partial Payment\",\"mode\":\"" + paymentType + "\",\"extra_fees\":" + fees + ",\"amount\":5000,\"transaction_id\":" + createLead.generatePhoneNumber() + ",\"paid_amount\":" + totalAmount + ",\"allow_excess\":false}";
    }

    public static String FullPayment() {
        return "{\"item\":\"Balance Payment\",\"mode\":\"IMPS\",\"extra_fees\":0,\"amount\":" + DealCreate.balanceAmount + ",\"transaction_id\":" + createLead.generatePhoneNumber() + ",\"paid_amount\":" + DealCreate.balanceAmount + ",\"allow_excess\":false}";
    }

    public static String dealUpdate() {
        return "{\r\n"
                + "  \"delivery_hub\": "+dealDetails.delivery_hub+",\r\n"
                + "  \"purchase_time\": \""+dealDetails.purchase_time+"\",\r\n"
                + "  \"target_delivery_time\": "+dealDetails.target_delivery_time+",\r\n"
                + "  \"rto_transferable_state\": \"uttar pradesh\",\r\n"
                + "  \"loan_confirmation\": "+dealDetails.loan_confirmation+",\r\n"
                + "  \"isDealTaskApiCall\": true\r\n"
                + "}";
    }

    public static String buyerUpdate(String name, String pan, String GST) {
        return "{\r\n"
                + "  \"name\": \"" + name + "\",\r\n"
                + "  \"care_of_relation\": \"S/O\",\r\n"
                + "  \"care_of_name\": \"Deepanshu\",\r\n"
                + "  \"email\": \"j@gmail.com\",\r\n"
                + "  \"contact_number\": \"\",\r\n"
                + "  \"gstin_no\": \""+GST+"\",\r\n"
                + "  \"pin_code\": \"\",\r\n"
                + "  \"city\": \"Noida\",\r\n"
                + "  \"state\": \"\",\r\n"
                + "  \"address\": \"Noida\",\r\n"
                + "  \"address_line_1\": \"\",\r\n"
                + "  \"landmark\": \"\",\r\n"
                + "  \"buyer_pan_number\": \"\",\r\n"
                + "  \"gender\": \"\",\r\n"
                + "  \"date_of_birth\": null,\r\n"
                + "  \"marital_status\": \"\",\r\n"
                + "  \"category\": \"assured\",\r\n"
                + "  \"pan_number\": \"" + pan + "\"\r\n"
                + "}";
    }

   
    public static String editBuyer(String name, String PAN, String type, String buyerContactNum) {
        return "{\r\n"
        		+ "  \"deal_id\": "+DealCreate.dealId+",\r\n"
        		+ "  \"category\": \""+createLead.leadCategory+"\",\r\n"
        		+ "  \"care_of_name\": \"dasd\",\r\n"
        		+ "  \"contact_number\": \""+buyerContactNum+"\",\r\n"
        		+ "  \"date_of_birth\": \"1999-09-03T18:30:00.000Z\",\r\n"
        		+ "  \"gstin_no\": \"\",\r\n"
        		+ "  \"name\": \""+name+"\",\r\n"
        		+ "  \"pan_number\": \""+PAN+"\",\r\n"
        		+ "  \"company_type\": null,\r\n"
        		+ "  \"buyer_type\": \""+type+"\",\r\n"
        		+ "  \"email\": \"ad@dsfsd.fff\",\r\n"
        		+ "  \"gender\": \"Female\",\r\n"
        		+ "  \"marital_status\": \"married\",\r\n"
        		+ "  \"pan_status\": \"not-verified\",\r\n"
        		+ "  \"gstin_status\": \"verification-successful\"\r\n"
        		+ "}";
    }
    
    public static String rcAddress()
    {
    	return "{\r\n"
    			+ "  \"category\": \""+createLead.leadCategory+"\",\r\n"
    			+ "  \"care_of_name\": \"hyhy\",\r\n"
    			+ "  \"contact_number\": \""+createLead.phoneNumber+"\",\r\n"
    			+ "  \"date_of_birth\": null,\r\n"
    			+ "  \"gstin_no\": null,\r\n"
    			+ "  \"name\": \" SHAAN CHOUDHARY\",\r\n"
    			+ "  \"pan_number\": \""+dealDetails.PAN+"\",\r\n"
    			+ "  \"company_type\": null,\r\n"
    			+ "  \"buyer_type\": \"Individual\",\r\n"
    			+ "  \"email\": \"ssheshant@gmail.com\",\r\n"
    			+ "  \"gender\": \"male\",\r\n"
    			+ "  \"marital_status\": \"\",\r\n"
    			+ "  \"address_line_1\": \"noida\",\r\n"
    			+ "  \"city\": \"Noida\",\r\n"
    			+ "  \"state\": \"Uttar Pradesh\",\r\n"
    			+ "  \"pin_code\": 201309,\r\n"
    			+ "  \"address_type\": \"rc_delivery_address\",\r\n"
    			+ "  \"parent_address_type\": \"new_address_on_rc\"\r\n"
    			+ "}";
    }

    public static String requestDeal() {
        return "{\r\n"
                + "  \"buy_lead_id\": \"" + createLead.leadId + "\",\r\n"
                + "  \"sell_lead_id\":\"" + addCars.sell_lead_id + "\",\r\n"
                + "  \"buyer_id\": \"" + DealCreate.buyerId + "\",\r\n"
                + "  \"deal_price\":\"" + addCars.dealAmount + "\",\r\n"
                + "  \"name\": \"" + DealCreate.name + "\",\r\n"
                + "  \"email\": \"" + DealCreate.buyerEmail + "\",\r\n"
                + "  \"contact_number\": \"" + createLead.phoneNumber + "\",\r\n"
                + "  \"payment_gateway\": \"cart_page_link\",\r\n"
                + "  \"loan_facilitation\": false,\r\n"
                + "  \"token_amount\": " + DealCreate.tokenAmount + ",\r\n"
                + "  \"exchange\": null\r\n"
                + "}";
    }

    public static String paymentUrl() 
    {
       return "{\r\n"
       		+ "  \"operationName\": \"listPaymentReceivables\",\r\n"
       		+ "  \"variables\": {\r\n"
       		+ "    \"contractTradeObjectType\": \"deal\",\r\n"
       		+ "    \"first\": \"10\",\r\n"
       		+ "    \"contractTradeObjectId\": \""+DealCreate.dealId+"\"\r\n"
       		+ "  },\r\n"
       		+ "  \"query\": \"query listPaymentReceivables($pk: Int, $first: Int, $before: String, $after: String, $paymentMode_In: [String], $tradingPartnerId_In: [Int], $cityId_In: [Int], $hubId_In: [Int], $registrationNo: String, $tradingPartnerId: Int, $cityId: Int, $procurementCategory: String, $transactionId: String, $paymentMode: String, $receivedDateTime_Gte: DateTime, $receivedDateTime_Lte: DateTime, $realizedDateTime_Gte: DateTime, $realizedDateTime_Lte: DateTime, $status: String, $hubId: Float, $createdById: Float, $contractTradeObjectId: Float, $contractTradeObjectType: String, $orderBy: String, $dealType: String) {\\n  listPaymentReceivables(\\n    pk: $pk\\n    first: $first\\n    before: $before\\n    after: $after\\n    paymentMode_In: $paymentMode_In\\n    tradingPartnerId_In: $tradingPartnerId_In\\n    cityId_In: $cityId_In\\n    hubId_In: $hubId_In\\n    registrationNo: $registrationNo\\n    tradingPartnerId: $tradingPartnerId\\n    cityId: $cityId\\n    procurementCategory: $procurementCategory\\n    transactionId: $transactionId\\n    paymentMode: $paymentMode\\n    receivedDateTime_Gte: $receivedDateTime_Gte\\n    receivedDateTime_Lte: $receivedDateTime_Lte\\n    realizedDateTime_Gte: $realizedDateTime_Gte\\n    realizedDateTime_Lte: $realizedDateTime_Lte\\n    status: $status\\n    hubId: $hubId\\n    createdById: $createdById\\n    contractTradeObjectId: $contractTradeObjectId\\n    contractTradeObjectType: $contractTradeObjectType\\n    orderBy: $orderBy\\n    dealType: $dealType\\n  ) {\\n    pageInfo {\\n      hasNextPage\\n      endCursor\\n      __typename\\n    }\\n    totalCount\\n    edgeCount\\n    edges {\\n      node {\\n        id\\n        pk\\n        transactionNumber\\n        feesPercent\\n        baseAmount\\n        transactions {\\n          settlementId\\n          transactionNumber\\n          __typename\\n        }\\n        paymentMode\\n        amount\\n        receivedDateTime\\n        realizedDateTime\\n        remarks\\n        status\\n        createdBy {\\n          fullName\\n          __typename\\n        }\\n        deal {\\n          pk\\n          finalAmount\\n          buyLead {\\n            contactDetails {\\n              name\\n              contactNumber\\n              city {\\n                displayName\\n                __typename\\n              }\\n              __typename\\n            }\\n            __typename\\n          }\\n          carPurchaseMeta {\\n            dealType\\n            __typename\\n          }\\n          dealHub {\\n            name\\n            __typename\\n          }\\n          buyer {\\n            currentContactDetails {\\n              name\\n              contactNumber\\n              __typename\\n            }\\n            __typename\\n          }\\n          bankLoanApplication {\\n            bank {\\n              name\\n              displayName\\n              __typename\\n            }\\n            __typename\\n          }\\n          sellLead {\\n            procurementCategory\\n            procurredcar {\\n              tradingPartner {\\n                name\\n                displayName\\n                __typename\\n              }\\n              __typename\\n            }\\n            profile {\\n              registrationNo\\n              city {\\n                displayName\\n                name\\n                __typename\\n              }\\n              __typename\\n            }\\n            __typename\\n          }\\n          paymentHistory {\\n            createdBy {\\n              fullName\\n              __typename\\n            }\\n            __typename\\n          }\\n          createdBy {\\n            fullName\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"\r\n"
       		+ "}";
    }

    public static String approvingPayment(String paymentType) {

        return "{\"operationName\":\"editReceivable\",\"variables\":{\"amount\":\"5000\",\"paymentRealizedDateTime\":\"2023-03-01T11:56:48+05:30\",\"receivablePk\":\""+DealCreate.receivablePkValue+"\",\"remarks\":\"asd\",\"status\":\"approved\",\"transactionNumber\":\""+createLead.generatetransaction()+"\",\"paymentMode\":\""+paymentType+"\"},\"query\":\"mutation editReceivable($amount: Float, $paymentRealizedDateTime: DateTime, $receivablePk: Int!, $remarks: String, $status: String!, $transactionNumber: String, $paymentMode: String) {\\n  editReceivable(\\n    amount: $amount\\n    paymentRealizedDateTime: $paymentRealizedDateTime\\n    receivablePk: $receivablePk\\n    remarks: $remarks\\n    status: $status\\n    transactionNumber: $transactionNumber\\n    paymentMode: $paymentMode\\n  ) {\\n    ok\\n    error\\n    __typename\\n  }\\n}\\n\"}";
       }

    public static String approvingFullPayment() {
        return "{\"operationName\":\"editReceivable\",\"variables\":{\"amount\":\"" + DealCreate.balanceAmount + "\",\"paymentRealizedDateTime\":\"2022-10-18T13:09:47+05:30\",\"receivablePk\":\"" + DealCreate.receivablePkValue + "\",\"status\":\"approved\",\"transactionNumber\":\"" + createLead.generatetransaction() + "\",\"paymentMode\":\"IMPS\"},\"query\":\"mutation editReceivable($amount: Float, $paymentRealizedDateTime: DateTime, $receivablePk: Int!, $remarks: String, $status: String!, $transactionNumber: String, $paymentMode: String) {\\n  editReceivable(\\n    amount: $amount\\n    paymentRealizedDateTime: $paymentRealizedDateTime\\n    receivablePk: $receivablePk\\n    remarks: $remarks\\n    status: $status\\n    transactionNumber: $transactionNumber\\n    paymentMode: $paymentMode\\n  ) {\\n    ok\\n    error\\n    __typename\\n  }\\n}\\n\"}";
    }

    public static String gettingBalance() {
        return "{\"operationName\":\"editReceivable\",\"variables\":{\"amount\":\"50000\",\"paymentRealizedDateTime\":\"2022-10-18T13:09:47+05:30\",\"receivablePk\":\"" + DealCreate.receivablePkValue + "\",\"status\":\"approved\",\"transactionNumber\":\"997889986\",\"paymentMode\":\"IMPS\"},\"query\":\"mutation editReceivable($amount: Float, $paymentRealizedDateTime: DateTime, $receivablePk: Int!, $remarks: String, $status: String!, $transactionNumber: String, $paymentMode: String) {\\n  editReceivable(\\n    amount: $amount\\n    paymentRealizedDateTime: $paymentRealizedDateTime\\n    receivablePk: $receivablePk\\n    remarks: $remarks\\n    status: $status\\n    transactionNumber: $transactionNumber\\n    paymentMode: $paymentMode\\n  ) {\\n    ok\\n    error\\n    __typename\\n  }\\n}\\n\"}";
    }

    public static String visit(String visitType, Object time) {
        return "{\r\n"
                + "    \"context_type\": \"buylead\",\r\n"
                + "    \"visit_type\": \"" + visitType + "\",\r\n"
                + "    \"location_type\": \"hub\",\r\n"
                + "    \"context_id\": \"" + createLead.leadId + "\",\r\n"
                + "    \"location_id\": 1,\r\n"
                + "    \"scheduled_time\": \"" + time + "\"\r\n"
                + "}";
    }

    public static String paymentFees(String payment) {
        return "{\r\n"
                + "  \"car_type\": \"" + createLead.leadCategory + "\",\r\n"
                + "  \"payment_mode\": \"" + payment + "\"\r\n"
                + "} ";
    }
// for exchange case and other cases
    static Response response;
    public static final String BASEURI = "https://sales-backend-test.spinnyworks.in/";
    public static final String KEY = "Keycloak-Authorization";

    public static void putData(String url, String body) {
        response = RestAssured.given().
                header(KEY, createLead.accessToken)
                //			.header("client",DefiningEnv.client)
                .contentType(ContentType.JSON)
                .body(body).put(BASEURI + url);
    }
    public static JsonPath post(String body, String url) {
        String urls = BASEURI + url;
        response = RestAssured.given().header(KEY, createLead.accessToken)
                //       .header("client",DefiningEnv.client)
                .contentType(ContentType.JSON).body(body).post(urls);
        String text = response.asString();
        return new JsonPath(text);
    }
    
    // for loan case
    public static void putLoan(String url, String body) {
        response = RestAssured.given().
                header(KEY, createLead.accessToken)
                		.header("client","loan")
                .contentType(ContentType.JSON)
                .body(body).put(BASEURI + url);
    }
// for loan case

    public static JsonPath postLoan(String body, String url) {
        String urls = BASEURI + url;
        response = RestAssured.given().header(KEY, createLead.accessToken)
                			.header("client","loan")
                .contentType(ContentType.JSON).body(body).post(urls);
        String text = response.prettyPrint();
        return new JsonPath(text);
    }

    public static JsonPath get(String url) {
        response = RestAssured.given().
                header(KEY, createLead.accessToken).header("client", "dev").contentType(ContentType.JSON)
                .get(BASEURI + url);
        return new JsonPath(response.prettyPrint());
    }


    public static String CancellationRequest() {
        return "{\r\n"
                + "    \"deal_id\": " + DealCreate.dealId + ",\r\n"
                + "    \"reason\": \"Cancelling to book another car\"\r\n"
                + "}";
    }
    
    public static String ReturnRequest() {
        return "{\r\n"
                + "    \"deal_id\": " + DealCreate.dealId + ",\r\n"
                + "    \"reason\": \"Didn�t like the car\"\r\n"
                + "}";
    }
    public static String ApprovingReturnRequest() {
        return "{\r\n"
        		+ "  \"status\": {\r\n"
        		+ "    \"name\": \"deal-flow-deal-flow-car-returned\",\r\n"
        		+ "    \"description\": \"Car Returned\"\r\n"
        		+ "  },\r\n"
        		+ "  \"comment\": \"Test\",\r\n"
        		+ "  \"reason\": \"Car Price\"\r\n"
        		+ "}";
    }

    public static String RejectCancellationRequest() {
        return "{\r\n"
                + "  \"id\": " + DealCreate.dealId + ",\r\n"
                + "  \"comment\": \"Test\"\r\n"
                + "}";
    }

    public static String ApprovingCancellationRequest() {
        return "{\r\n"
        		+ "	\"status\":{\r\n"
        		+ "		\"name\":\"deal-flow-deal-flow-deal-cancelled\",\r\n"
        		+ "		\"description\":\"Deal Cancelled\"\r\n"
        		+ "	},\r\n"
        		+ "	\"comment\":\"this is cancellation reason\",\r\n"
        		+ "	\"reasons_dict\":{\"Not buying any car\":\"Unsure if I need car\"}\r\n"
        		+ "}";
    }

    public static String testDrive() {
        return "{\r\n"
                + "    \"at_home\": " + Visit.HTD + ",\r\n"
                + "    \"delivery_interested\": false,\r\n"
                + "    \"visit_address\": {\r\n"
                + "        \"address_line_1\": \"Ghaziabad\",\r\n"
                + "        \"address\": \"Ghaziabad, Uttar Pradesh, India\",\r\n"
                + "        \"lat\": 28.6691565,\r\n"
                + "        \"long\": 77.45375779999999,\r\n"
                + "        \"city_name\": \"Ghaziabad\",\r\n"
                + "        \"block_society_name\": \"yrd\",\r\n"
                + "        \"landmark\": \"jfdjjf\",\r\n"
                + "        \"house_flat_number\": \"jfkjfk\",\r\n"
                + "        \"pin_code\": \"201012\"\r\n"
                + "    },\r\n"
                + "    \"sell_lead\": " + addCars.sell_lead_id + ",\r\n"
                + "    \"visit_hub_distance\": 50,\r\n"
                + "    \"context_type\": \"buylead\",\r\n"
                + "    \"visit_type\": \"test-drive\",\r\n"
                + "    \"location_type\": \"hub\",\r\n"
                + "    \"context_id\": " + createLead.leadId + ",\r\n"
                + "    \"location_id\": 2,\r\n"
                + "    \"scheduled_time\": \"" + Visit.timeSlot + "\"\r\n"
                + "}";
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'08:00:00");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Using today's date
        c.add(Calendar.DATE, 1); // Adding 1 days
        String dateTime = sdf.format(c.getTime());
        //System.out.println(dateTime);
        return dateTime;
    }
    
    public static String getUserStatus() {
    return "{\r\n"
    		+ "    \"contact_number\": \""+createLead.phoneNumber+"\"\r\n"
    		+ "}";
    }
    
    public static String sendOtpBody () {
    	return "{\r\n"
    			+ "    \"contact_number\": \""+createLead.phoneNumber+"\",\r\n"
    			+ "    \"whatsapp\": false,\r\n"
    			+ "    \"code_len\": 4\r\n"
    			+ "}";
    }
    
public static String sendLoginDetails() {
	return "{\r\n"
			+ "    \"contact_number\": \""+createLead.phoneNumber+"\",\r\n"
			+ "    \"full_name\": \"jatin\",\r\n"
			+ "    \"email\": \"\",\r\n"
			+ "    \"verification_code\": \""+DatabaseConnection.name+"\",\r\n"
			+ "    \"password\": \"\",\r\n"
			+ "    \"event\": \"signUpNotVerified\",\r\n"
			+ "    \"whatsapp_optin\": true,\r\n"
			+ "    \"device_id\": null,\r\n"
			+ "    \"is_relogin\": false\r\n"
			+ "}";
}
public static String getCallBack()
{
	return "{\r\n"
			+ "    \"intent\": \"listing-callback\",\r\n"
			+ "    \"status\": \"new\",\r\n"
			+ "    \"city\": {\r\n"
			+ "        \"name\": \"bangalore\",\r\n"
			+ "        \"display_name\": \"Bangalore\"\r\n"
			+ "    },\r\n"
			+ "    \"noEvent\": false,\r\n"
			+ "    \"contact_details\": {\r\n"
			+ "        \"name\": \"jatin\",\r\n"
			+ "        \"contact_number\": \""+createLead.phoneNumber+"\"\r\n"
			+ "    },\r\n"
			+ "    \"marketing_attribution\": "+DatabaseConnection.marketingId+"\r\n"
			+ "}";
}
public static String getMarketingAttribution()
{
	return "{\r\n"
			+ "    \"contact_number\": \""+createLead.phoneNumber+"\",\r\n"
			+ "    \"utm_source\": \"direct\",\r\n"
			+ "    \"utm_medium\": \"\",\r\n"
			+ "    \"utm_campaign\": \"\",\r\n"
			+ "    \"utm_content\": \"\",\r\n"
			+ "    \"utm_term\": \"\",\r\n"
			+ "    \"partner_id\": \"\",\r\n"
			+ "    \"source\": \"\",\r\n"
			+ "    \"sub_source\": \"\",\r\n"
			+ "    \"medium\": \"\",\r\n"
			+ "    \"campaign\": \"\",\r\n"
			+ "    \"term\": \"\",\r\n"
			+ "    \"content\": \"\",\r\n"
			+ "    \"gclid\": \"\",\r\n"
			+ "    \"fbclid\": \"\",\r\n"
			+ "    \"user_agent\": \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36\",\r\n"
			+ "    \"device_type\": \"desktop\",\r\n"
			+ "    \"device_name\": \"Windows\",\r\n"
			+ "    \"os_version\": \"10\",\r\n"
			+ "    \"visit_time\": \""+DatabaseConnection.marketingTime+"\"\r\n"
			+ "}";
}

public static String getCamundaResponse()
{
	return "{\r\n"
			+ "    \"variables\": {\r\n"
			+ "        \"source\": {\r\n"
			+ "            \"value\": \"callback\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"sub_source\": {\r\n"
			+ "            \"value\": \"NA\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"otp_verified\": {\r\n"
			+ "            \"value\": \"yes\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"loan_eligibility\": {\r\n"
			+ "            \"value\": \"no\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"historical_data\": {\r\n"
			+ "            \"value\": \"no\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"customer_scheduled_visit\": {\r\n"
			+ "            \"value\": \"no\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"user_activity_received\": {\r\n"
			+ "            \"value\": \"no\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"buy_lead_id\": {\r\n"
			+ "            \"value\": \""+DatabaseConnection.buyLead+"\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"city\": {\r\n"
			+ "            \"value\": \"5.0\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"platform_source\": {\r\n"
			+ "            \"value\": \"web\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        },\r\n"
			+ "        \"utm_medium\": {\r\n"
			+ "            \"value\": \"Direct\",\r\n"
			+ "            \"type\": \"string\"\r\n"
			+ "        }\r\n"
			+ "    }\r\n"
			+ "}";
}


public static String setInsurance()
{
	return "{\r\n"
			+ "    \"company_id\": 31,\r\n"
			+ "    \"issue_date\": \"2023-01-01T00:00:00.000+05:30\",\r\n"
			+ "    \"amount\": 899999,\r\n"
			+ "    \"expiry_date\": \"2025-12-31T00:00:00.000+05:30\",\r\n"
			+ "    \"insurance_type\": \"Comprehensive\",\r\n"
			+ "    \"nominee_details\": {\r\n"
			+ "        \"id\": "+dealDetails.NomineeID+",\r\n"
			+ "        \"name\": \"Kavya\",\r\n"
			+ "        \"age\": "+dealDetails.NomineeAge+",\r\n"
			+ "        \"relation\": \"wife\",\r\n"
			+ "        \"dob\": \"1993-09-24\",\r\n"
			+ "        \"gender\": \"female\"\r\n"
			+ "    }\r\n"
			+ "}";
}

public static String setNominee()
{
	return "{\r\n"
			+ "    \"company_id\": null,\r\n"
			+ "    \"issue_date\": null,\r\n"
			+ "    \"amount\": 0,\r\n"
			+ "    \"expiry_date\": \"2022-09-30\",\r\n"
			+ "    \"insurance_type\": \"Comprehensive\",\r\n"
			+ "    \"nominee_details\": {\r\n"
			+ "        \"id\": null,\r\n"
			+ "        \"name\": \"Kavya\",\r\n"
			+ "        \"age\": null,\r\n"
			+ "        \"relation\": \"wife\",\r\n"
			+ "        \"dob\": \"1993-12-24T11:49:00.000Z\",\r\n"
			+ "        \"gender\": \"male\"\r\n"
			+ "    }\r\n"
			+ "}";
}

public static String getMileage()
{
	return "{\"operationName\":\"leadDetails\",\"variables\":{\"leadId\":75062},\"query\":\"query leadDetails($leadId: ID) {\\n  leadDetails(leadId: $leadId) {\\n    edges {\\n      node {\\n        id\\n        variantRank\\n        listingPrice {\\n          id\\n          targetObjectId\\n          value\\n          active\\n          __typename\\n        }\\n        buyBackPrice\\n        published\\n        isFixedPrice\\n        model {\\n          name\\n          bodyType\\n          segmentType\\n          overview\\n          __typename\\n        }\\n        marketPrice\\n        onRoadPrice\\n        bestEmi {\\n          minLoanAge\\n          minLoanAmount\\n          maxLoanAmount\\n          maxLoanAge\\n          bestLoanAge\\n          bestLoanAmount\\n          bestEmi\\n          roi\\n          __typename\\n        }\\n        profile {\\n          id\\n          registrationNo\\n          registrationYear\\n          registrationMonth\\n          fuelType\\n          transmissionType\\n          makeYear\\n          makeMonth\\n          mileage\\n          rto {\\n            code\\n            district {\\n              name\\n              displayName\\n              __typename\\n            }\\n            __typename\\n          }\\n          noOfOwners\\n          insuranceType\\n          insuranceCompany\\n          insuranceValue\\n          insurancePremium\\n          insuranceProjection {\\n            year\\n            month\\n            __typename\\n          }\\n          roadTaxActualValidity {\\n            month\\n            year\\n            day\\n            __typename\\n          }\\n          roadTaxProjection {\\n            month\\n            year\\n            day\\n            __typename\\n          }\\n          color\\n          make {\\n            name\\n            countryOfOrigin\\n            parentCompany\\n            __typename\\n          }\\n          variant {\\n            id\\n            displayName\\n            __typename\\n          }\\n          hub {\\n            name\\n            shortName\\n            pk\\n            __typename\\n          }\\n          location\\n          __typename\\n        }\\n        procurementCategory\\n        carStatus\\n        upcomingSupply\\n        isRefurb\\n        estimatedDateOfArrival\\n        serviceHistoryChecks {\\n          createdTime\\n          lastUpdatedTime\\n          serviceDate\\n          mileage\\n          comment\\n          acceptable\\n          isValid\\n          createdBy {\\n            username\\n            fullName\\n            email\\n            contactNumber\\n            __typename\\n          }\\n          tags\\n          __typename\\n        }\\n        features {\\n          name\\n          displayName\\n          __typename\\n        }\\n        tyreCondition {\\n          label\\n          condition\\n          photos\\n          __typename\\n        }\\n        carUspFeatures {\\n          key\\n          attributes {\\n            displayName\\n            priority\\n            value\\n            description\\n            imageUrl\\n            __typename\\n          }\\n          __typename\\n        }\\n        carUspSpecifications {\\n          key\\n          attributes {\\n            displayName\\n            priority\\n            value\\n            logoUrl\\n            __typename\\n          }\\n          __typename\\n        }\\n        productFeatures {\\n          key\\n          attributes {\\n            displayName\\n            value\\n            priority\\n            imageUrl\\n            description\\n            __typename\\n          }\\n          __typename\\n        }\\n        productSpecifications {\\n          key\\n          attributes {\\n            displayName\\n            value\\n            priority\\n            logoUrl\\n            __typename\\n          }\\n          __typename\\n        }\\n        refurbImage {\\n          imageUrl\\n          imageLabel\\n          createdAt\\n          __typename\\n        }\\n        images {\\n          key\\n          fileSet {\\n            file {\\n              id\\n              rawurl\\n              __typename\\n            }\\n            fileLabel {\\n              name\\n              displayName\\n              __typename\\n            }\\n            __typename\\n          }\\n          __typename\\n        }\\n        dents {\\n          id\\n          x\\n          y\\n          type\\n          priority\\n          photo {\\n            id\\n            rawurl\\n            __typename\\n          }\\n          __typename\\n        }\\n        inspectionReportVersion\\n        inspectionReport {\\n          displayName\\n          source\\n          values {\\n            status\\n            label\\n            __typename\\n          }\\n          __typename\\n        }\\n        __typename\\n      }\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n\"}";
}

public static String cookie() {
	// TODO Auto-generated method stub
	return "_hjSessionUser_1522085=eyJpZCI6ImFmMmRiYjdkLTE2MWUtNTcyOC04Mjc2LTJiZmY0YTk1YjkzNCIsImNyZWF0ZWQiOjE2OTIzNDY4MDMwNTgsImV4aXN0aW5nIjp0cnVlfQ==; _hjSessionUser_2067151=eyJpZCI6ImQ2N2VkODY2LTIyMjItNThjZS1iMTI0LWVhYjEzZWFhMzE1MiIsImNyZWF0ZWQiOjE2OTIzNTg1MDY2NzIsImV4aXN0aW5nIjp0cnVlfQ==; utm_source=direct; platform=web; varnishPrefixHome=true; _gcl_au=1.1.1835412731.1692960543; token=null; csrftoken=qlxoPFUG7rGwRB02a6fRj1NeAMsTi88qUwP0a4ffl5VnRkE6AgQcvNMZhXSYLL8C; _ga_Z4H235Q9J5=GS1.1.1693197686.2.0.1693197686.0.0.0; _gid=GA1.2.1781445001.1694407541; _ga_DWBB12WNQR=GS1.2.1694585595.30.0.1694585603.0.0.0; session_state=d99d79e3-db62-4792-9874-335422c5631a; _clck=1qjqg6j|2|fez|0|1325; _ga_L6Q7CLKFGN=GS1.2.1694588309.1.0.1694588309.0.0.0; mp_69953b010d581b182508e892b4f561ad_mixpanel=%7B%22distinct_id%22%3A%208469055%2C%22%24device_id%22%3A%20%2218a086d7b3b66f-0c22c4217b6d62-26031c51-e1000-18a086d7b3b66f%22%2C%22%24initial_referrer%22%3A%20%22%24direct%22%2C%22%24initial_referring_domain%22%3A%20%22%24direct%22%2C%22%24user_id%22%3A%208469055%7D; _clsk=1tceu4g|1694589435851|6|1|s.clarity.ms/collect; keycloak_session=9c83_05cpeSMvLlqL0zqFQ|1694601772|6THD8fqEgzTuflOMdpg15Ihtmcs|06007c88-06f0-49b4-b967-d667fc9926c3|fc86092a-c6a7-4705-8c84-687826efc178; _hjIncludedInSessionSample_1522085=0; _hjSession_1522085=eyJpZCI6IjQzNmQzMDM2LTA4MzktNDM0Ny1hNDdjLWU5M2E2OGI2ODdlMyIsImNyZWF0ZWQiOjE2OTQ1OTQ3MzUwNzQsImluU2FtcGxlIjpmYWxzZX0=; _hjAbsoluteSessionInProgress=0; _role=city-ops-manager; _ga=GA1.1.724867358.1692346803; _ga_7WLVFG8MB8=GS1.1.1694594735.68.1.1694596747.60.0.0\r\n"
			+ "";
}


}

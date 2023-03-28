package Handler;

import java.util.Map;
import java.util.function.Function;
import org.json.*;

class Request {
    private String httpMethod;
    private Map<String, String> headers;
    private String body;

    public String getBody() {
        return body;
    }
}

class Response {
    private int statusCode;
    private String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}

class GameSet {
    final String[] PROPERTIES = {"color", "shape", "count", "fill"};
    JSONArray _cards;
    JSONArray setCards;

    public GameSet(JSONArray cards) {
        _cards = cards;
    }

    public JSONArray findSet() {
        JSONArray setCards = new JSONArray();
        for (int i = 0; i < _cards.length(); i++) {
            JSONObject[] cards = new JSONObject[3];
            cards[0] = _cards.getJSONObject(i);
            for (int j = i + 1; j < _cards.length(); j++) {
                cards[1] = _cards.getJSONObject(j);
                for (int k = j + 1; k < _cards.length(); k++) {
                    cards[2] = _cards.getJSONObject(k);
                    boolean flagSet = true;
                    for (String property :
                            PROPERTIES)
                        if (!checkCardsProperty(cards[0], cards[1], cards[2], property)) {
                            flagSet = false;
                            break;
                        }
                    if (flagSet) {
                        setCards.put(cards[0]);
                        setCards.put(cards[1]);
                        setCards.put(cards[2]);
                        return setCards;
                    }
                }
            }
        }
        return new JSONArray();
    }


    public boolean checkCardsProperty(JSONObject firstCard, JSONObject secondCard, JSONObject thirdCard,
                                   String property) {
        int firstProperty = firstCard.getInt(property);
        int secondProperty = secondCard.getInt(property);
        int thirdProperty = thirdCard.getInt(property);
        return (firstProperty == secondProperty &&
                secondProperty == thirdProperty) || (
                        firstProperty != secondProperty && secondProperty != thirdProperty &&
                                firstProperty != thirdProperty
                );
    }
}

public class Handler implements Function<Request, Response> {
    @Override
    public Response apply(Request request) {
        JSONObject jsonObject = new JSONObject(request.getBody());
        GameSet gameSet = new GameSet(jsonObject.getJSONArray("cards"));
        String setCards = gameSet.findSet().toString();
        return new Response(200, "{\"status\": \"ok\", \"setCards\":" + setCards + "}");
    }
}



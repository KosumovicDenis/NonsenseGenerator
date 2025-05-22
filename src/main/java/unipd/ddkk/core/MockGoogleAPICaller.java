package unipd.ddkk.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockGoogleAPICaller extends GoogleAPICaller {
    private final ObjectMapper mapper = new ObjectMapper();
    private final Random random = new Random();

    @Override
    protected JsonNode callApi(String sentence) {
        ArrayNode categoriesArray = mapper.createArrayNode();

        String[] categoryNames = {
                "Toxic", "Insult", "Profanity", "Derogatory", "Sexual", "Death, Harm & Tragedy",
                "Violent", "Firearms & Weapons", "Public Safety", "Health", "Religion & Belief",
                "Illicit Drugs", "War & Conflict", "Politics", "Finance", "Legal"
        };

        for (String name : categoryNames) {
            ObjectNode categoryNode = mapper.createObjectNode();
            categoryNode.put("name", name);
            categoryNode.put("confidence", Math.round(random.nextFloat() * 51) / 100.0f); // Rounded to 2 decimals
            categoriesArray.add(categoryNode);
        }

        return categoriesArray;
    }

    // Optional: override getModerationCategories to log or simulate further
    // behavior if needed
    @Override
    public ArrayList<PhraseClassificationAttribute> getModerationCategories(String sentence) {
        System.out.println("[MOCK] Using mock moderation data for sentence: " + sentence);
        return super.getModerationCategories(sentence);
    }

    @Override
    public SentenceStructure getStructure(String sentence) {
        List<String> nouns = new ArrayList<>();
        List<String> verbs = new ArrayList<>();
        List<String> adjectives = new ArrayList<>();

        String json;
        if (random.nextBoolean()) {
            // Option A
            nouns.add("Today");
            nouns.add("day");
            verbs.add("is");
            adjectives.add("good");
            json = """
                                      {
                              "sentences": [
                                {
                                  "text": {
                                    "content": "Today is a good day",
                                    "beginOffset": -1
                                  }
                                }
                              ],
                              "tokens": [
                                {
                                  "text": {
                                    "content": "Today",
                                    "beginOffset": -1
                                  },
                                  "partOfSpeech": {
                                    "tag": "NOUN",
                                    "aspect": "ASPECT_UNKNOWN",
                                    "case": "CASE_UNKNOWN",
                                    "form": "FORM_UNKNOWN",
                                    "gender": "GENDER_UNKNOWN",
                                    "mood": "MOOD_UNKNOWN",
                                    "number": "SINGULAR",
                                    "person": "PERSON_UNKNOWN",
                                    "proper": "PROPER_UNKNOWN",
                                    "reciprocity": "RECIPROCITY_UNKNOWN",
                                    "tense": "TENSE_UNKNOWN",
                                    "voice": "VOICE_UNKNOWN"
                                  },
                                  "dependencyEdge": {
                                    "headTokenIndex": 1,
                                    "label": "NSUBJ"
                                  },
                                  "lemma": "Today"
                                },
                                {
                                  "text": {
                                    "content": "is",
                                    "beginOffset": -1
                                  },
                                  "partOfSpeech": {
                                    "tag": "VERB",
                                    "aspect": "ASPECT_UNKNOWN",
                                    "case": "CASE_UNKNOWN",
                                    "form": "FORM_UNKNOWN",
                                    "gender": "GENDER_UNKNOWN",
                                    "mood": "INDICATIVE",
                                    "number": "SINGULAR",
                                    "person": "THIRD",
                                    "proper": "PROPER_UNKNOWN",
                                    "reciprocity": "RECIPROCITY_UNKNOWN",
                                    "tense": "PRESENT",
                                    "voice": "VOICE_UNKNOWN"
                                  },
                                  "dependencyEdge": {
                                    "headTokenIndex": 1,
                                    "label": "ROOT"
                                  },
                                  "lemma": "be"
                                },
                                {
                                  "text": {
                                    "content": "a",
                                    "beginOffset": -1
                                  },
                                  "partOfSpeech": {
                                    "tag": "DET",
                                    "aspect": "ASPECT_UNKNOWN",
                                    "case": "CASE_UNKNOWN",
                                    "form": "FORM_UNKNOWN",
                                    "gender": "GENDER_UNKNOWN",
                                    "mood": "MOOD_UNKNOWN",
                                    "number": "NUMBER_UNKNOWN",
                                    "person": "PERSON_UNKNOWN",
                                    "proper": "PROPER_UNKNOWN",
                                    "reciprocity": "RECIPROCITY_UNKNOWN",
                                    "tense": "TENSE_UNKNOWN",
                                    "voice": "VOICE_UNKNOWN"
                                  },
                                  "dependencyEdge": {
                                    "headTokenIndex": 4,
                                    "label": "DET"
                                  },
                                  "lemma": "a"
                                },
                                {
                                  "text": {
                                    "content": "good",
                                    "beginOffset": -1
                                  },
                                  "partOfSpeech": {
                                    "tag": "ADJ",
                                    "aspect": "ASPECT_UNKNOWN",
                                    "case": "CASE_UNKNOWN",
                                    "form": "FORM_UNKNOWN",
                                    "gender": "GENDER_UNKNOWN",
                                    "mood": "MOOD_UNKNOWN",
                                    "number": "NUMBER_UNKNOWN",
                                    "person": "PERSON_UNKNOWN",
                                    "proper": "PROPER_UNKNOWN",
                                    "reciprocity": "RECIPROCITY_UNKNOWN",
                                    "tense": "TENSE_UNKNOWN",
                                    "voice": "VOICE_UNKNOWN"
                                  },
                                  "dependencyEdge": {
                                    "headTokenIndex": 4,
                                    "label": "AMOD"
                                  },
                                  "lemma": "good"
                                },
                                {
                                  "text": {
                                    "content": "day",
                                    "beginOffset": -1
                                  },
                                  "partOfSpeech": {
                                    "tag": "NOUN",
                                    "aspect": "ASPECT_UNKNOWN",
                                    "case": "CASE_UNKNOWN",
                                    "form": "FORM_UNKNOWN",
                                    "gender": "GENDER_UNKNOWN",
                                    "mood": "MOOD_UNKNOWN",
                                    "number": "SINGULAR",
                                    "person": "PERSON_UNKNOWN",
                                    "proper": "PROPER_UNKNOWN",
                                    "reciprocity": "RECIPROCITY_UNKNOWN",
                                    "tense": "TENSE_UNKNOWN",
                                    "voice": "VOICE_UNKNOWN"
                                  },
                                  "dependencyEdge": {
                                    "headTokenIndex": 1,
                                    "label": "ATTR"
                                  },
                                  "lemma": "day"
                                }
                              ],
                              "language": "en"
                            }
                    """;
        } else {
            // Option B
            nouns.add("cat");
            nouns.add("sky");
            verbs.add("is");
            verbs.add("raining");
            adjectives.add("blue");
            adjectives.add("yellow");
            json = """
                    {
                      "sentences": [
                        {
                          "text": {
                            "content": "The cat is blue and the sky is yellow, it's raining",
                            "beginOffset": -1
                          }
                        }
                      ],
                      "tokens": [
                        {
                          "text": {
                            "content": "The",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "DET",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 1,
                            "label": "DET"
                          },
                          "lemma": "The"
                        },
                        {
                          "text": {
                            "content": "cat",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "NOUN",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "SINGULAR",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "NSUBJ"
                          },
                          "lemma": "cat"
                        },
                        {
                          "text": {
                            "content": "is",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "VERB",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "INDICATIVE",
                            "number": "SINGULAR",
                            "person": "THIRD",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "PRESENT",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "ROOT"
                          },
                          "lemma": "be"
                        },
                        {
                          "text": {
                            "content": "blue",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "ADJ",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "ACOMP"
                          },
                          "lemma": "blue"
                        },
                        {
                          "text": {
                            "content": "and",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "CONJ",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "CC"
                          },
                          "lemma": "and"
                        },
                        {
                          "text": {
                            "content": "the",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "DET",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 6,
                            "label": "DET"
                          },
                          "lemma": "the"
                        },
                        {
                          "text": {
                            "content": "sky",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "NOUN",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "SINGULAR",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 7,
                            "label": "NSUBJ"
                          },
                          "lemma": "sky"
                        },
                        {
                          "text": {
                            "content": "is",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "VERB",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "INDICATIVE",
                            "number": "SINGULAR",
                            "person": "THIRD",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "PRESENT",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "CONJ"
                          },
                          "lemma": "be"
                        },
                        {
                          "text": {
                            "content": "yellow",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "ADJ",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 7,
                            "label": "ACOMP"
                          },
                          "lemma": "yellow"
                        },
                        {
                          "text": {
                            "content": ",",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "PUNCT",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "P"
                          },
                          "lemma": ","
                        },
                        {
                          "text": {
                            "content": "it",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "PRON",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "NOMINATIVE",
                            "form": "FORM_UNKNOWN",
                            "gender": "NEUTER",
                            "mood": "MOOD_UNKNOWN",
                            "number": "SINGULAR",
                            "person": "THIRD",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 12,
                            "label": "NSUBJ"
                          },
                          "lemma": "it"
                        },
                        {
                          "text": {
                            "content": "'s",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "VERB",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "INDICATIVE",
                            "number": "SINGULAR",
                            "person": "THIRD",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "PRESENT",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 12,
                            "label": "AUX"
                          },
                          "lemma": "be"
                        },
                        {
                          "text": {
                            "content": "raining",
                            "beginOffset": -1
                          },
                          "partOfSpeech": {
                            "tag": "VERB",
                            "aspect": "ASPECT_UNKNOWN",
                            "case": "CASE_UNKNOWN",
                            "form": "FORM_UNKNOWN",
                            "gender": "GENDER_UNKNOWN",
                            "mood": "MOOD_UNKNOWN",
                            "number": "NUMBER_UNKNOWN",
                            "person": "PERSON_UNKNOWN",
                            "proper": "PROPER_UNKNOWN",
                            "reciprocity": "RECIPROCITY_UNKNOWN",
                            "tense": "TENSE_UNKNOWN",
                            "voice": "VOICE_UNKNOWN"
                          },
                          "dependencyEdge": {
                            "headTokenIndex": 2,
                            "label": "PARATAXIS"
                          },
                          "lemma": "rain"
                        }
                      ],
                      "language": "en"
                    }

                                        """;
        }

        JsonNode node = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            node = mapper.readTree(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new SentenceStructure(
                nouns.toArray(new String[0]),
                verbs.toArray(new String[0]),
                adjectives.toArray(new String[0]),
                node);
    }
}

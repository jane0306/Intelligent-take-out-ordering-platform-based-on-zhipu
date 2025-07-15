package com.example.delivery.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.delivery.R;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AIChatActivity extends AppCompatActivity {
    private RecyclerView rvMessages;
    private EditText etMessage;
    private MessageAdapter adapter;
    private List<Message> messageList = new ArrayList<>();
    private OkHttpClient client;
    private static final String API_KEY = "a64d332c118e4fc1a412ffafa93d845c.eDjzu1fwTdolxsBO";
    private static final String API_URL = "https://open.bigmodel.cn/api/paas/v4/chat/completions";

    // 系统提示词，定义AI角色
    private static final String SYSTEM_PROMPT = "你现在是智能特惠外卖助手芙芙，专门为用户提供经济实惠的外卖选择建议。\n"
            + "你的任务是：\n"
            + "1. 根据用户提供的外卖信息，分析并推荐最优惠的选择方案\n"
            + "2. 比较不同餐厅的性价比，考虑价格、分量和口味\n"
            + "3. 推荐使用优惠券、满减活动等省钱技巧\n"
            + "4. 可以建议合理的套餐组合\n"
            + "5. 使用亲切友好的语气，可以适当使用表情符号\n"
            + "6. 如果信息不足，主动询问用户的预算、口味偏好等必要信息";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_chat);

        // 初始化视图
        rvMessages = findViewById(R.id.rv_messages);
        etMessage = findViewById(R.id.et_message);
        Button btnSend = findViewById(R.id.btn_send);

        // 初始化RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(layoutManager);
        adapter = new MessageAdapter(messageList);
        rvMessages.setAdapter(adapter);

        // 初始化OkHttp
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        // 添加欢迎消息
        addAIMessage("你好呀～我是你的外卖省钱小助手芙芙！(◕‿◕✿)\n"
                + "我可以帮你找到最经济实惠的外卖选择～\n"
                + "请告诉我：\n"
                + "- 你想吃什么类型的美食\n"
                + "- 你的预算范围\n"
                + "- 有没有特别的口味偏好");

        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addUserMessage(message);
                etMessage.setText("");
                sendToAI(message);
            }
        });
    }

    private void sendToAI(String question) {
        try {
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "chatglm_pro");

            JSONArray messages = new JSONArray();

            // 添加系统角色设定
            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", SYSTEM_PROMPT);
            messages.put(systemMsg);

            // 添加历史对话
            for (Message msg : messageList) {
                JSONObject msgObj = new JSONObject();
                msgObj.put("role", msg.getType() == Message.TYPE_USER ? "user" : "assistant");
                msgObj.put("content", msg.getContent());
                messages.put(msgObj);
            }

            // 添加最新用户消息
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", question);
            messages.put(userMsg);

            requestBody.put("messages", messages);

            // 构建请求
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(
                            requestBody.toString(),
                            MediaType.parse("application/json; charset=utf-8")
                    ))
                    .build();

            // 发送异步请求
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        addAIMessage("哎呀，芙芙遇到了一点小问题，请再试一次哦~");
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            JSONArray choices = result.getJSONArray("choices");
                            String answer = choices.getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");
                            runOnUiThread(() -> {
                                addAIMessage(answer);
                            });
                        } catch (JSONException e) {
                            runOnUiThread(() -> {
                                addAIMessage("解析响应失败，请重试");
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            addAIMessage("请求失败（" + response.code() + "），芙芙暂时无法回答");
                        });
                    }
                }
            });
        } catch (JSONException e) {
            runOnUiThread(() -> {
                addAIMessage("构建请求失败，请检查输入");
            });
        }
    }

    private void addUserMessage(String message) {
        messageList.add(new Message(message, Message.TYPE_USER));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvMessages.scrollToPosition(messageList.size() - 1);
    }

    private void addAIMessage(String message) {
        messageList.add(new Message(message, Message.TYPE_AI));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvMessages.scrollToPosition(messageList.size() - 1);
    }

    // 消息模型类（保持不变）
    public static class Message {
        public static final int TYPE_USER = 0;
        public static final int TYPE_AI = 1;
        private String content;
        private int type;

        public Message(String content, int type) {
            this.content = content;
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public int getType() {
            return type;
        }
    }

    // RecyclerView适配器（保持不变）
    public static class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        private List<Message> mMessageList;

        public MessageAdapter(List<Message> messages) {
            mMessageList = messages;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(viewType == Message.TYPE_USER ?
                            R.layout.item_user_message : R.layout.item_ai_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Message message = mMessageList.get(position);
            holder.tvContent.setText(message.getContent());
        }

        @Override
        public int getItemViewType(int position) {
            return mMessageList.get(position).getType();
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvContent;
            ViewHolder(View view) {
                super(view);
                tvContent = view.findViewById(R.id.tv_message_content);
            }
        }
    }
}
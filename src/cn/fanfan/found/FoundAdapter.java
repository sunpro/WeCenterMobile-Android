package cn.fanfan.found;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.fanfan.common.Config;
import cn.fanfan.common.MyGridAdapter;
import cn.fanfan.common.ShowPic;
import cn.fanfan.detail.essay.EssayDetailActivity;
import cn.fanfan.detail.question.QuestionDetailActivity;
import cn.fanfan.main.R;
import cn.fanfan.topic.imageload.ImageDownLoader;
import cn.fanfan.userinfo.UserInfoShowActivity;


public class FoundAdapter extends BaseAdapter{
		   private List<FoundItem> newitems;
		   private Context context;
		   private ImageDownLoader imageDownLoader;
	     public FoundAdapter(List<FoundItem> comitems,Context context,ImageDownLoader imageDownLoader) {
			// TODO Auto-generated constructor stub
	    	
	    	 super();
	    	 this.newitems = comitems;
	    	 this.context = context;
	    	 this.imageDownLoader = imageDownLoader;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newitems.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return newitems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			ViewHodler hodler;
			final String mImageUrl = Config.getValue("userImageBaseUrl")+newitems.get(arg0).getAvatar_file();
			 if(arg1 == null){
				    hodler = new ViewHodler();			
					arg1 = LayoutInflater.from(context).inflate(R.layout.found_question, null);
					hodler.name = (TextView)arg1.findViewById(R.id.username);
					hodler.question = (TextView)arg1.findViewById(R.id.quescontent);
					hodler.userimage = (ImageView)arg1.findViewById(R.id.userimage);
					hodler.tag = (TextView)arg1.findViewById(R.id.tag);
					hodler.gridView = (GridView) arg1.findViewById(R.id.gridView);
					arg1.setTag(hodler);
				
			} else {
				hodler = (ViewHodler)arg1.getTag();
			}
			 hodler.gridView.setTag(newitems.get(arg0).getQuestion_id()+"gridview");
			 hodler.userimage.setTag(mImageUrl);
			 arg1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						// TODO Auto-generated method stub
						if (newitems.get(arg0).getType().equals("question")) {
							intent.putExtra("questionid", newitems.get(arg0).getQuestion_id());
							intent.setClass(context, QuestionDetailActivity.class);
							
						} else {
							intent.putExtra("eid", newitems.get(arg0).getQuestion_id());
							intent.setClass(context, EssayDetailActivity.class);
						}
						
						context.startActivity(intent);
					}
				});
			 hodler.name.setText(newitems.get(arg0).getName());
			 hodler.question.setText(newitems.get(arg0).getQuestion());
			switch (newitems.get(arg0).getInttag()) {
			case 0:
				hodler.tag.setText("发起了问题");
				break;
			case 1:
				hodler.tag.setText("回复了问题");
				break;
			case 2:
				hodler.tag.setText("发表了文章");
				break;
			default:
				break;
			}
			 Bitmap bitmap = imageDownLoader.getCacheBitmap(mImageUrl.replaceAll("[^\\w]", ""));
			
				if (bitmap != null) {
					
					hodler.userimage.setImageBitmap(bitmap);
				} else {
					hodler.userimage.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.ic_avatar_default));
				}
				hodler.userimage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(context, UserInfoShowActivity.class);
						intent.putExtra("uid", newitems.get(arg0).getUid());
						context.startActivity(intent);
					}
				});
				 final ArrayList<String> urls = newitems.get(arg0).getUrls();
				if (urls != null && urls.size()>0 ) {
					hodler.gridView.setVisibility(View.VISIBLE);
					hodler.gridView.setAdapter(new MyGridAdapter(newitems.get(arg0).getThumbs(), context));
					hodler.gridView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							// TODO Auto-generated method stub
							Intent intent = new Intent();
							intent.putStringArrayListExtra("images", urls);
				        	intent.putExtra("tag",arg2);
				        	intent.setClass(context, ShowPic.class);
				        	context.startActivity(intent);
						}
					});
				} else {
					hodler.gridView.setVisibility(View.GONE);
				}
			return arg1;
		}
		class ViewHodler{
			private TextView name,question,tag;
			private ImageView userimage;
			private GridView gridView;
	}  
}


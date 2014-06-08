package com.example.managecontacts;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {
	
	/** Custom List Adapter allowing each row of the ListView to be customised */
	
	/** Variables */
	private static List<Contacts> searchList;
	private LayoutInflater inflater;

	/** Constructor for the custom adapter */
	public ContactsAdapter(Context context, List<Contacts> contacts) {
		searchList = contacts;
		inflater = LayoutInflater.from(context);
	}

	/** Method which creates a new View for every row of the ListView */
	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder contactsHolder;
		/** If the view is null, inflate the custom row */
		if (convertView == null) {
			/** Inflate the custom row for the view */
			convertView = inflater.inflate(R.layout.custom_contacts_row, null);
			contactsHolder = new ViewHolder();
			contactsHolder.contactName = (TextView) convertView.findViewById(R.id.contact);
			convertView.setTag(contactsHolder);
		} else {
			contactsHolder = (ViewHolder) convertView.getTag();
		}

		contactsHolder.contactName.setText(searchList.get(position).getName());

		return convertView;
	}

	/** Method to get the size of the List */
	@Override
	public int getCount() {
		return searchList.size();
	}

	/** Method to return specific item */
	@Override
	public Object getItem(int position) {
		return searchList.get(position);
	}

	/** Method to return item id */
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder {
		TextView contactName;
	}
}
package com.example.john.miniproj3;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by John on 25/4/2016.
 */
public class EngDataFragment extends ListFragment implements
        SearchView.OnQueryTextListener, SearchView.OnCloseListener {
    private static final String STATE_QUERY="q";
    private CharSequence initialQuery=null;
    private SearchView sv=null;

    private DatabaseHelper db=null;
    private AsyncTask task=null;
    private SimpleCursorAdapter adapter=null;
    private Cursor mCursor = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return inflater.inflate(R.layout.fragment_eng_data, container, false);
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        if (savedInstanceState == null) {
            initAdapter();
        }
        else {
            initialQuery=savedInstanceState.getCharSequence(STATE_QUERY);
        }
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel(false);
        }

        ((CursorAdapter)getListAdapter()).getCursor().close();
        db.close();

        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        if (!sv.isIconified()) {
            state.putCharSequence(STATE_QUERY, sv.getQuery());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);

        configureSearchView(menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(getActivity(), new Intent(getActivity(), MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            adapter.getFilter().filter("");
        }
        else {
            adapter.getFilter().filter(newText.toString());
        }

        return(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return(false);
    }

    @Override
    public boolean onClose() {
        adapter.getFilter().filter("");

        return(true);
    }

    private void configureSearchView(Menu menu) {
        MenuItem search=menu.findItem(R.id.search);

        sv=(SearchView) MenuItemCompat.getActionView(search);
        sv.setOnQueryTextListener(this);
        sv.setOnCloseListener(this);
        sv.setSubmitButtonEnabled(false);
        sv.setIconifiedByDefault(true);

        if (initialQuery != null) {
            sv.setIconified(false);
            search.expandActionView();
            sv.setQuery(initialQuery, true);
        }
    }

    private void initAdapter() {

        adapter=
                new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, mCursor,
                        new String[] {DatabaseHelper.NAME},
                        new int[] {android.R.id.text1}, 0);

        setListAdapter(adapter);

        if (mCursor==null) {
            db=new DatabaseHelper(getActivity().getApplicationContext());
            task=new LoadCursorTask().execute();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, final long id) {
        Cursor c = (Cursor) l.getAdapter().getItem(position);
        String title = c.getString(c.getColumnIndex(DatabaseHelper.NAME));
        String value = c.getString(c.getColumnIndex(DatabaseHelper.DESC));

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View addView=inflater.inflate(R.layout.add_edit, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_title).setView(addView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values=new ContentValues();
                        AlertDialog dlg=(AlertDialog)dialog;
                        EditText name=(EditText)dlg.findViewById(R.id.phrase_name);
                        EditText desc=(EditText)dlg.findViewById(R.id.phrase_desc);

                        values.put(DatabaseHelper.NAME, name.getText().toString());
                        values.put(DatabaseHelper.DESC, desc.getText().toString());
                        values.put("ROWID", String.valueOf(id));

                        task=new UpdateTask().execute(values);
                    }
                })
                .setNeutralButton(R.string.cancel, null)
                .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        task=new DeleteTask().execute(String.valueOf(id));
                    }
                }).show();


        ((EditText)addView.findViewById(R.id.phrase_name)).setText(title);
        ((EditText)addView.findViewById(R.id.phrase_desc)).setText(value);
    }

    private void add() {
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View addView=inflater.inflate(R.layout.add_edit, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_title).setView(addView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContentValues values=new ContentValues();
                        AlertDialog dlg=(AlertDialog)dialog;
                        EditText name=(EditText)dlg.findViewById(R.id.phrase_name);
                        EditText desc=(EditText)dlg.findViewById(R.id.phrase_desc);

                        values.put(DatabaseHelper.NAME, name.getText().toString());
                        values.put(DatabaseHelper.DESC, desc.getText().toString());
                        values.put(DatabaseHelper.LANG, "eng");

                        task=new InsertTask().execute(values);
                    }
                })
                .setNegativeButton(R.string.cancel, null).show();
    }

    abstract private class BaseTask<T> extends AsyncTask<T, Void, Cursor> {
        @Override
        public void onPostExecute(Cursor result) {
            ((CursorAdapter)getListAdapter()).changeCursor(result);
            task=null;
        }

        protected Cursor doQuery() {
            Cursor result=
                    db
                            .getReadableDatabase()
                            .query(DatabaseHelper.TABLE,
                                    new String[] {"ROWID AS _id",
                                            DatabaseHelper.NAME,
                                            DatabaseHelper.DESC},
                                    DatabaseHelper.LANG + "= ?", new String[] {"eng"}, null, null, DatabaseHelper.NAME);

            result.getCount();

            return(result);
        }
    }

    private class LoadCursorTask extends BaseTask<Void> {
        @Override
        protected Cursor doInBackground(Void... params) {
            return(doQuery());
        }
    }

    private class InsertTask extends BaseTask<ContentValues> {
        @Override
        protected Cursor doInBackground(ContentValues... values) {
            db.getWritableDatabase().insert(DatabaseHelper.TABLE,
                    DatabaseHelper.NAME, values[0]);

            return(doQuery());
        }
    }

    private class UpdateTask extends BaseTask<ContentValues> {
        @Override
        protected Cursor doInBackground(ContentValues... values) {
            db.getWritableDatabase().update(DatabaseHelper.TABLE, values[0], "ROWID = ?", new String[] {values[0].getAsString("ROWID")});
            return doQuery();
        }
    }

    private class DeleteTask extends BaseTask<String> {
        @Override
        protected Cursor doInBackground(String... values) {
            db.getWritableDatabase().delete(DatabaseHelper.TABLE, "ROWID = ?", new String[] {values[0]});
            return doQuery();
        }
    }
}

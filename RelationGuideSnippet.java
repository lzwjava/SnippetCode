package com.avoscloud.chat;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lzw on 15/7/20.
 */
public class RelationGuideSnippet {
  public void pointerOneToOneSave() {
    AVObject game = new AVObject("Game");
    game.put("createdBy", AVUser.getCurrentUser());
  }

  void pointerOneToOneQuery() {
    AVQuery gameQuery = new AVQuery<>("Game");
    gameQuery.whereEqualTo("createdBy", AVUser.getCurrentUser());
  }

  void pointerOneToOneGet() {
    // say we have a Game object
    AVObject game =...;
    // getting the user who created the Game
    AVUser createdBy = game.getAVUser("createdBy");
  }

  void avobjectArraySave() {
    // let's say we have four weapons
    AVObject scimitar =...
    AVObject plasmaRifle =...
    AVObject grenade =...
    AVObject bunnyRabbit =...

    // stick the objects in an array

    List<AVObject> weapons = new ArrayList<>();
    weapons.add(scimitar);
    weapons.add(plasmaRifle);
    weapons.add(grenade);
    weapons.add(bunnyRabbit);

    // store the weapons for the user
    AVUser.getCurrentUser().put("weaponsList", weapons);

  }

  void avobjectArrayGet() {
    List<AVObject> weapons = AVUser.getCurrentUser().getList("weaponsList");
  }

  void avobjectArrayQueryIncludeKey() {
    // set up our query for a User object
    AVQuery<AVUser> userQuery = AVUser.getQuery();

    // configure any constraints on your query...
    // for example, you may want users who are also playing with or against you

    // tell the query to fetch all of the Weapon objects along with the user
    // get the "many" at the same time that you're getting the "one"
    userQuery.include("weaponsList");

    // execute the query
    userQuery.findInBackground(new FindCallback<AVUser>() {
      @Override
      public void done(List<AVUser> list, AVException e) {
        // list contains all of the User objects, and their associated Weapon objects, too
      }
    });
  }

  void avobjectArrayQueryContainedIn() {
    AVQuery<AVUser> userQuery = AVUser.getQuery();

    // add a constraint to query for whenever a specific Weapon is in an array
    userQuery.whereEqualTo("weaponsList", scimitar);

    // or query using an array of Weapon objects...
    userQuery.whereContainedIn("weaponsList", arrayOfWeapons);
  }

  void avrelationSave () {
    // let’s say we have a few objects representing Author objects
    AVObject authorOne = …
    AVObject authorTwo = …
    AVObject authorThree = …

    // now we create a book object
    AVObject book= new AVObject("Book");

    // now let’s associate the authors with the book
    // remember, we created a "authors" relation on Book
    AVRelation<AVObject> relation = book.getRelation("authors");
    // make sure these objects should be saved to server before adding to relation

    relation.add(authorOne);
    relation.add(authorTwo);
    relation.add(authorThree);;

    // now save the book object
    book.saveInBackground();
  }

  void avrelationQuery() {
    // suppose we have a book object
    AVObject book = ...

    // create a relation based on the authors key
    AVRelation relation = book.getRelation("authors");

    // generate a query based on that relation
    AVQuery<AVObject> query = relation.getQuery();

    // now execute the query
  }


  void avrelationQueryEqualTo() {
    // suppose we have a author object, for which we want to get all books
    AVObject author = ...

    // first we will create a query on the Book object
    AVQuery query = new AVQuery<>("Book");

    // now we will query the authors relation to see if the author object
    // we have is contained therein
    query.whereEqualTo("authors", author);
  }

  void relatedClassSave() {
    // suppose we have a user we want to follow
    AVUser otherUser = ...

    // create an entry in the Follow table
    AVObject follow = new AVObject("Follow");
    follow.put("from", AVUser.getCurrentUser());
    follow.put("to", otherUser);
    follow.put("date", new Date());
    follow.saveInBackground();
  }

  void relatedClassQueryFrom () {
    // set up the query on the Follow table
    AVQuery<AVObject> query = new AVQuery("Follow");
    query.whereEqualTo("from", AVUser.getCurrentUser());

    // execute the query
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        for(AVObject o : list) {
          // o is an entry in the Follow table
          // to get the user, we get the object with the to key
          AVUser otherUser = o.getAVUser("to");

          // to get the time when we followed this user, get the date key
          Date when = o.getDate("date");
        }
      }
    });
  }

  void relatedClassQueryTo () {
    // set up the query on the Follow table
    AVQuery<AVObject> query = new AVQuery("Follow");
    query.whereEqualTo("to", AVUser.getCurrentUser());

    // execute the query
    query.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        for(AVObject o : list) {
          // o is an entry in the Follow table
          // to get the user, we get the object with the from key
          AVUser otherUser = o.getAVUser("from");

          // to get the time the user was followed, get the date key
          Date when = o.getDate("date");
        }
      }
    });
  }

  void avobjectArrayManyToManySave() {
    // let's say we have an author
    AVObject author = ...

    // and let's also say we have an book
    AVObject book = ...

    // add the author to the authors list for the book
    book.add("authors", author);
  }

  void avobjectArrayManyToManyQuery() {
    // set up our query for the Book object
    AVQuery<AVObject> bookQuery = [AVQuery queryWithClassName:@"Book"];

    // configure any constraints on your query...
    // tell the query to fetch all of the Author objects along with the Book
    bookQuery.include("authors");

    // execute the query
    bookQuery.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        // list is all of the Book objects, and their associated
        // Author objects, too
      }
    });
  }

  void avobjectArrayManyToManyGet() {
    AVObject book;
    List<AVObject> authorList = book.getList("authors");
  }

  void avobjectArrayManyToManyQueryEqualTo () {
    // suppose we have an Author object
    AVObject author = ...

    // set up our query for the Book object
    AVQuery<AVObject> bookQuery = new AVQuery("Book");

    // configure any constraints on your query...
    bookQuery.whereEqualTo("authors", author);

    // tell the query to fetch all of the Author objects along with the Book
    bookQuery.include("authors");

    // execute the query
    bookQuery.findInBackground(new FindCallback<AVObject>() {
      @Override
      public void done(List<AVObject> list, AVException e) {
        // list is all of the Book objects, and their associated Author objects, too
      }
    });
  }

}

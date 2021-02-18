package com.haoxuan.demo.controller;

import com.haoxuan.demo.Entity.*;
import com.haoxuan.demo.Helper.Helper;
import com.haoxuan.demo.MySQL.DB;
import org.hibernate.sql.Delete;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final Helper help = new Helper();

    /*
    * This is public Api
    * return all the books
    * */
    @RequestMapping(value="all",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public List<BookTable> getBooks(HttpServletResponse response){

        try {
            //query all the books from database and return
            List<BookTable> info = DB.getInstance().queryAllBooks();

            return info;
        }catch (Exception e){
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }

        return null;
    }

    /*
     * This is authenticated Api
     * add new book
     * */
    @RequestMapping(value="add",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    public CreateBookResponse createBook(@RequestBody CreateBookRequest book,HttpServletResponse response){

        try{

            CreateBookResponse res = new CreateBookResponse();
            //get user info from login
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }else {
                userName = principal.toString();
            }
            //fist check user info
            UserTable info = DB.getInstance().queryUser(userName);

            //check required parameters
            if(book.getAuthor().length()<=0 ||book.getIsbn().length()<=0 ||book.getTitle().length()<=0 ||book.getPublished_date().length()<=0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }



            BookTable bookInfo = new BookTable();
            bookInfo.setId(help.genUUID());
            bookInfo.setTitle(book.getTitle());
            bookInfo.setAuthor(book.getAuthor());
            bookInfo.setIsbn(book.getIsbn());
            bookInfo.setPublished_date(book.getPublished_date());
            bookInfo.setBook_created(help.getDate());
            bookInfo.setUser_id(info.getId());

            boolean status = DB.getInstance().addBook(bookInfo);

            if(status){
                response.setStatus(HttpServletResponse.SC_CREATED);
                res.setId(bookInfo.getId());
                res.setTitle(bookInfo.getTitle());
                res.setAuthor(bookInfo.getAuthor());
                res.setIsbn(bookInfo.getIsbn());
                res.setPublished_date(bookInfo.getPublished_date());
                res.setBook_created(bookInfo.getBook_created());
                res.setUser_id(bookInfo.getUser_id());
                return res;

            }else{
                response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
                return null;
            }

        }catch (Exception e){
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }


        return null;
    }

    /*
     * This is authenticated Api
     * query book info by book id
     * */
    @RequestMapping(value="queryBookById",method = RequestMethod.GET,produces = {"application/json;charset=UTF-8"})
    public BookTable queryBookById(@RequestBody QueryBookByIdRequest request,HttpServletResponse response){

        try{
            //check required parameters
            if(request.getId().length()<=0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }

            BookTable book =DB.getInstance().queryBookById(request.getId());

            if(book.getId()==null){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
            }

            return book;

        }catch (Exception e){
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }


        return null;
    }


    /*
     * This is authenticated Api
     * delete book info by book id
     * */
    @RequestMapping(value="deleteBookById",method = RequestMethod.DELETE,produces = {"application/json;charset=UTF-8"})
    public void deleteBookById(@RequestBody DeleteBookByIdRequest request, HttpServletResponse response){

        try{
            //check required parameters
            if(request.getId().length()<=0){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            //get user info from login
            String userName = null;
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }else {
                userName = principal.toString();
            }
            //fist check user info
            UserTable info = DB.getInstance().queryUser(userName);

            BookTable book =DB.getInstance().queryBookById(request.getId());

            if(book.getId() == null){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            //delete the book only for who created it
            if(!info.getId().equals(book.getUser_id())){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            DB.getInstance().deleteBookById(request.getId());

            response.setStatus(HttpServletResponse.SC_NO_CONTENT);


        }catch (Exception e){
            //e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
        }

    }
}

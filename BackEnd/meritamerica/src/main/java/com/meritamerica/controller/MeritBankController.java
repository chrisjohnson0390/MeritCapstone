package com.meritamerica.controller; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.meritamerica.exceptions.ExceedsCombinedBalanceLimitException;
import com.meritamerica.exceptions.NegativeAmountException;
import com.meritamerica.exceptions.NotFoundException;
import com.meritamerica.models.*;
import com.meritamerica.repos.*;
import com.meritamerica.security.JwtUtil;
import com.meritamerica.security.MyUserDetailsServices;

@RestController
public class MeritBankController {
	List<String> strings = new ArrayList<String>(); 
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsServices userDetailsServices;
	@Autowired
	private JwtUtil jwtTokenUtil;
	@Autowired
	private AccountHolderContactDetailsRepository accountHolderContactRepo;
	@Autowired
	private AccountHolderRepository accountHolderRepo;
	@Autowired
	private CDAccountRepository cdAccountRepo;
	@Autowired
	private CDOfferingRepository cdOfferingRepo;
	@Autowired
	private CheckingAccountRepository checkingAccountRepo;
	@Autowired
	private SavingsAccountRepository savingsAccountRepo;
	@Autowired
	private UsersRepository usersRepository;

	//..............................................................................
	@PostMapping(value = "/register")
	public ResponseEntity<?> register(@RequestBody AccountHolder data){
		accountHolderRepo.save(data);
		return ResponseEntity.ok(data);
	}
	@PostMapping(value = "/login")
	public AccountHolder login(@RequestBody String username){
		AccountHolder users = accountHolderRepo.findByUsername(username);
		Long x = users.getId();
		return accountHolderRepo.findById(x.longValue());
	}
	//..............................................................................
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)throws Exception
	{ 
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())	
					);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect Credentials" ,e);
		}
		final UserDetails userDetails = userDetailsServices
				.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		//If successful we will call on a 201 status and the payload in the status will pass through
		//the response.
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	
	@PostMapping(value = "/authenticate/CreateUser")
	public ResponseEntity<?> createUser(@RequestBody Users users){
		usersRepository.save(users);
		return ResponseEntity.ok(users);
	}
	//The following endpoints have be adjusted; there was an issues with the methods and the type mismatching.
	//The solution was to add a .longValue() after get ID because ID type was Long and we needed to get the long value 
	@GetMapping(value = "/Me")
	public AccountHolder getMe(@RequestHeader (name = "Authorization")String token){
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		Long x = users.getAccount().getId();
		return accountHolderRepo.findById(x.longValue());
	}
	
	@PostMapping(value = "/Me/CheckingAccounts")
	public CheckingAccount addMeChecking(@RequestHeader (name = "Authorization")String token, @RequestBody CheckingAccount checking) throws ExceedsCombinedBalanceLimitException, NegativeAmountException {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		AccountHolder account = accountHolderRepo.findById(users.getAccount().getId().longValue()); 
		account.addCheckingAccount(checking);
		checkingAccountRepo.save(checking); 
		return checking; 
	}
	
	@GetMapping(value = "/Me/CheckingAccounts")
	public List<CheckingAccount> getMeChecking(@RequestHeader (name = "Authorization")String token) {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		return accountHolderRepo.findById(users.getAccount().getId().longValue()).getCheckingAccounts();
	}
	
	@PostMapping(value = "/Me/SavingsAccounts")
	public SavingsAccount addMeSavings(@RequestHeader (name = "Authorization")String token, @RequestBody SavingsAccount savings) throws ExceedsCombinedBalanceLimitException, NegativeAmountException {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		AccountHolder account = accountHolderRepo.findById(users.getAccount().getId().longValue());
		account.addSavingsAccount(savings);
		accountHolderRepo.save(account);
		return savings;
	}
	
	@GetMapping(value = "/Me/SavingsAccounts")
	public List<SavingsAccount> getMeSavings(@RequestHeader (name = "Authorization")String token) {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		return accountHolderRepo.findById(users.getAccount().getId().longValue()).getSavingsAccounts();
	}
	
	@PostMapping(value = "/Me/CDAccounts")
	public CDAccount addMeCDAccount(@RequestHeader (name = "Authorization")String token, @RequestBody CDAccount cdAccount) throws ExceedsCombinedBalanceLimitException, NegativeAmountException {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		AccountHolder account = accountHolderRepo.findById(users.getAccount().getId().longValue());
		account.addCDAccount(cdAccount);
		accountHolderRepo.save(account);
		return cdAccount;
	}
	
	@GetMapping(value = "/Me/CDAccounts")
	public List<CDAccount> getMeCDAccount(@RequestHeader (name = "Authorization")String token) {
		token = token.substring(7);
		Users users = usersRepository.findByUsername(jwtTokenUtil.extractUsername(token)).get();
		return accountHolderRepo.findById(users.getAccount().getId().longValue()).getCDAccounts();
	}


}

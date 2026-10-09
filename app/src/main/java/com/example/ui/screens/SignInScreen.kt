package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onSignInSuccess: (name: String, role: String, company: String) -> Unit,
    onBackToWelcome: () -> Unit
) {
    var email by remember { mutableStateOf("john.smith@granvilleaudit.co") }
    var password by remember { mutableStateOf("••••••••") }
    var rememberMe by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign In", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackToWelcome) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Navy900,
                    navigationIconContentColor = Navy900
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Welcome back",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Navy900
            )
            Text(
                text = "Sign in to access your inspection projects, snagging audits, and generate PDF reports.",
                fontSize = 14.sp,
                color = Navy700
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Fast Demo Role Selectors
            Text(
                text = "QUICK DEMO ACCESS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Slate400,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onSignInSuccess("John Smith", "Lead Field Auditor", "Granville Audit Co.")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("demo_login_auditor"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Slate50
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("John Smith", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
                        Text("Auditor", fontSize = 11.sp, color = Emerald600)
                    }
                }

                OutlinedButton(
                    onClick = {
                        onSignInSuccess("Frank Vaughn", "Civil Engineer", "Structures Ltd.")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("demo_login_engineer"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Slate50
                    )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Frank Vaughn", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Navy900)
                        Text("Engineer", fontSize = 11.sp, color = Amber600)
                    }
                }
            }

            HorizontalDivider(color = Slate200, modifier = Modifier.padding(vertical = 4.dp))

            // Form inputs
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Navy700) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signin_email_input"),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Navy900,
                    unfocusedBorderColor = Slate200
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Navy700) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("signin_password_input"),
                shape = RoundedCornerShape(10.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Navy900,
                    unfocusedBorderColor = Slate200
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = Navy900)
                    )
                    Text("Remember me", fontSize = 14.sp, color = Navy800)
                }
                TextButton(onClick = { /* Simulated password reset */ }) {
                    Text("Forgot?", color = Sky500, fontWeight = FontWeight.SemiBold)
                }
            }

            Button(
                onClick = {
                    onSignInSuccess(
                        if (email.contains("john")) "John Smith" else "Site Supervisor",
                        "Lead Field Auditor",
                        "Granville Audit Co."
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("signin_submit_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Navy900)
            ) {
                Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Surface(
                color = Slate50,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Slate200),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = Emerald600)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Offline-First: All audits and snag photos are automatically saved locally on this device.",
                        fontSize = 12.sp,
                        color = Navy700,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

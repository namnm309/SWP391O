import React, { useState } from "react";
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  TextField,
  InputAdornment,
  IconButton,
  Pagination,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Rating,
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import ReplyIcon from "@mui/icons-material/Reply";

interface Feedback {
  id: number;
  patientName: string;
  date: string;
  rating: number;
  comment: string;
  reply?: string;
}

const initialFeedbacks: Feedback[] = [
  { id: 1, patientName: "John Doe", date: "2023-05-15", rating: 4, comment: "Great service, very friendly staff." },
  { id: 2, patientName: "Jane Smith", date: "2023-05-16", rating: 5, comment: "Excellent experience, highly recommended." },
  { id: 3, patientName: "Alice Johnson", date: "2023-05-17", rating: 3, comment: "Good service, but long waiting times." },
  { id: 4, patientName: "Bob Williams", date: "2023-05-18", rating: 4, comment: "Professional and efficient." },
  { id: 5, patientName: "Emma Brown", date: "2023-05-19", rating: 5, comment: "Outstanding care and attention." },
];

const FeedbackList: React.FC = () => {
  const [feedbacks, setFeedbacks] = useState<Feedback[]>(initialFeedbacks);
  const [searchTerm, setSearchTerm] = useState("");
  const [page, setPage] = useState(1);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [currentFeedback, setCurrentFeedback] = useState<Feedback | null>(null);

  const feedbacksPerPage = 5;

  const filteredFeedbacks = feedbacks.filter((feedback) =>
    feedback.patientName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const paginatedFeedbacks = filteredFeedbacks.slice((page - 1) * feedbacksPerPage, page * feedbacksPerPage);

  const handleOpenDialog = (feedback: Feedback) => {
    setCurrentFeedback(feedback);
    setDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setDialogOpen(false);
    setCurrentFeedback(null);
  };

  const handleSaveFeedback = (feedback: Feedback) => {
    setFeedbacks(feedbacks.map((f) => (f.id === feedback.id ? feedback : f)));
    handleCloseDialog();
  };

  return (
    <Box sx={{ flexGrow: 1, p: 3 }}>
      <Typography variant="h4" component="h2" gutterBottom>
        Feedback List
      </Typography>
      <Box sx={{ display: "flex", justifyContent: "space-between", mb: 3 }}>
        <TextField
          variant="outlined"
          placeholder="Search feedbacks..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchIcon />
                </InputAdornment>
              )
            }
          }}
          sx={{ width: "100%" }}
        />
      </Box>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Patient Name</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Rating</TableCell>
              <TableCell>Comment</TableCell>
              <TableCell>Reply</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {paginatedFeedbacks.map((feedback) => (
              <TableRow key={feedback.id}>
                <TableCell>{feedback.patientName}</TableCell>
                <TableCell>{feedback.date}</TableCell>
                <TableCell>
                  <Rating value={feedback.rating} readOnly />
                </TableCell>
                <TableCell>{feedback.comment}</TableCell>
                <TableCell>{feedback.reply || "-"}</TableCell>
                <TableCell>
                  <IconButton color="primary" onClick={() => handleOpenDialog(feedback)}>
                    <ReplyIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <Box sx={{ display: "flex", justifyContent: "center", mt: 3 }}>
        <Pagination
          count={Math.ceil(filteredFeedbacks.length / feedbacksPerPage)}
          page={page}
          onChange={(event, value) => setPage(value)}
          color="primary"
        />
      </Box>

      <Dialog open={dialogOpen} onClose={handleCloseDialog}>
        <DialogTitle>Reply to Feedback</DialogTitle>
        <DialogContent>
          {currentFeedback && (
            <Box
              component="form"
              sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}
            >
              <Typography variant="body1">
                <strong>Patient:</strong> {currentFeedback.patientName}
              </Typography>
              <Typography variant="body1">
                <strong>Comment:</strong> {currentFeedback.comment}
              </Typography>
              <TextField
                label="Reply"
                multiline
                rows={4}
                value={currentFeedback.reply || ""}
                onChange={(e) =>
                  setCurrentFeedback({
                    ...currentFeedback,
                    reply: e.target.value,
                  } as Feedback)
                }
                fullWidth
              />
            </Box>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Cancel</Button>
          <Button onClick={() => currentFeedback && handleSaveFeedback(currentFeedback)} color="primary">
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default FeedbackList;
